package com.paranoid.runordie.providers;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.helpers.database.TrackCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.httpResponses.SaveTrackResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.utils.SnackbarUtils;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

public class SaveTrackProvider {

    public static void saveTrack(Track track) {
        if (!App.getInstance().getState().isTrackSaving()) {
            Log.d("TAG", "Saving track..");
            App.getInstance().getState().setTrackSaving(true);
            saveTrackAsync(track).getResult();
        }
    }

    private static Task<Void> saveTrackAsync(final Track track) {
        return saveDBAsync(track).onSuccessTask(new Continuation<Track, Task<Track>>() {
            @Override
            public Task<Track> then(Task<Track> task) {
                return saveServerAsync(track);
            }
        }).onSuccessTask(new Continuation<Track, Task<Void>>() {
            @Override
            public Task<Void> then(Task<Track> task) {
                return refreshServerId(task.getResult());
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                if (task.isCompleted()) {
                    Log.d("TAG", "Saving track: SUCCESS");
                } else if (task.isFaulted()) {
                    Log.d("TAG", "Saving track: ERROR " + task.getError().getMessage());
                }
                App.getInstance().getState().setTrackSaving(false);
                return null;
            }
        });
    }

    private static Task<Track> saveDBAsync(final Track track) {
        return Task.call(new Callable<Track>() {
            @Override
            public Track call() {
                long dbId = TrackCrudHelper.insertTrackNoServerId(track);
                track.setDbId(dbId);
                return track;
            }
        });
    }

    private static Task<Track> saveServerAsync(final Track track) {
        Log.d("TAG", "Saving track on server..");

        final TaskCompletionSource<Track> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().saveTrack(track, new Callback<SaveTrackResponse>() {
            @Override
            public void success(SaveTrackResponse result) {
                Log.d("TAG", "Saving track on server: SUCCESS");
                track.setServerId(result.getId());
                tcs.setResult(track);
            }

            @Override
            public void failure(NetworkException exception) {
                Log.e("TAG", "Saving track on server: ERROR " + exception.getErrorCode());
                SnackbarUtils.showSnack(exception);
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }

    private static Task<Void> refreshServerId(final Track track) {
        return Task.call(new Callable<Void>() {
            @Override
            public Void call() {
                TrackCrudHelper.updateTrackServerId(track);
                return null;
            }
        });
    }
}
