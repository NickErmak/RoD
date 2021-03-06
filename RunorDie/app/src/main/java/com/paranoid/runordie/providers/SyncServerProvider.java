package com.paranoid.runordie.providers;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.helpers.database.TrackCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.httpResponses.SaveTrackResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

class SyncServerProvider {

    protected static void syncServerWithDb() {
        if (!App.getInstance().getState().isTrackSynchronized()) {
            Task.callInBackground(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    return syncServerWithDbAsync().getResult();
                }
            });
        }
    }

    private static Task<Void> syncServerWithDbAsync() {
        return loadUnsyncTracks().onSuccessTask(new Continuation<List<Track>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<Track>> task) throws Exception {
                Log.d("TAG", "sync: server synchronization with DB..");
                List<Task<Void>> trackUpdateTasks = new LinkedList<>();
                List<Track> unsyncTracks = task.getResult();
                for (Track track : unsyncTracks) {
                    trackUpdateTasks.add(syncTrack(track));
                }
                return Task.whenAll(trackUpdateTasks);
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                if (task.isCompleted()) {
                    App.getInstance().getState().setTrackSynchronized(true);
                    Log.d("TAG", "sync: server synchronization with DB: SUCCESS");
                }

                if (task.isFaulted()) {
                    Log.e("TAG", "sync: server synchronization with DB: ERROR " + task.getError());
                }
                return null;
            }
        },  Task.UI_THREAD_EXECUTOR);
    }

    private static Task<List<Track>> loadUnsyncTracks() {
        return Task.call(new Callable<List<Track>>() {
            @Override
            public List<Track> call() throws Exception {
                return TrackCrudHelper.loadTracksNoServerId();
            }
        });
    }

    private static Task<Void> syncTrack(Track track) {
        return sendToServer(track).onSuccessTask(new Continuation<Track, Task<Void>>() {
            @Override
            public Task<Void> then(Task<Track> task) throws Exception {
                return updateTrackServerId(task.getResult());
            }
        });
    }

    private static Task<Track> sendToServer(final Track track) {
        final TaskCompletionSource<Track> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().saveTrack(track, new Callback<SaveTrackResponse>() {
            @Override
            public void success(SaveTrackResponse result) {
                track.setServerId(result.getId());
                tcs.setResult(track);
            }

            @Override
            public void failure(NetworkException exception) {
                tcs.setError(exception);
            }
        });

        return tcs.getTask();
    }

    private static Task<Void> updateTrackServerId(final Track track) {
        return Task.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TrackCrudHelper.updateTrackServerId(track);
                return null;
            }
        });
    }
}
