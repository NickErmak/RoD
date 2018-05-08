package com.paranoid.runordie.server;

import android.database.Cursor;
import android.util.Log;

import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.TrackListResult;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

public class NetworkProvider {

    public static void syncDbWithServer() {
        syncDbWithServerAsync().getResult();
    }

    private static Task<Void> syncDbWithServerAsync() {

        return Task.callInBackground(new Callable<List<TrackListResult>>() {
            @Override
            public List<TrackListResult> call() throws Exception {
                return Task.whenAllResult(loadTrackLists()).getResult();
            }

        }).onSuccessTask(new Continuation<List<TrackListResult>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<TrackListResult>> task) {
                List<Track> serverTracks = null,
                        dbTracks = null;
                for (TrackListResult result : task.getResult()) {
                    if (result.getType() == TrackListResult.TYPE_FROM.SERVER) {
                        serverTracks = result.getTracks();
                    }
                    if (result.getType() == TrackListResult.TYPE_FROM.DATABASE) {
                        dbTracks = result.getTracks();
                    }
                }
                return refreshDbAsync(serverTracks, dbTracks);
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                if (task.isCompleted()) {
                    Log.d("TAG", "synchronization: OK");
                    HomeBroadcast.sendBroadcast(HomeBroadcast.ACTION.TRACKS_REFRESHED);
                }
                if (task.isFaulted()) {
                    Log.e("TAG", "synchronization: ERROR " + task.getError().getMessage());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    private static List<Task<TrackListResult>> loadTrackLists() {
        List<Task<TrackListResult>> tasks = new ArrayList<>();
        Task<TrackListResult> serverLoadTask = getServerTracksAsync();
        Task<TrackListResult> dbLoadTask = getDbTrackIdAsync();
        try {
            serverLoadTask.waitForCompletion();
            dbLoadTask.waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tasks.add(serverLoadTask);
        tasks.add(dbLoadTask);
        return tasks;
    }

    private static Task<TrackListResult> getDbTrackIdAsync() {
        return Task.callInBackground(new Callable<TrackListResult>() {
            @Override
            public TrackListResult call() {
                Log.d("TAG", "loading tracks from DB..");
                List<Track> dbTrackList = DbCrudHelper.loadTracksServerIdOnly();
                Log.d("TAG", "loading tracks from DB: SUCCESS");
                return new TrackListResult(
                        TrackListResult.TYPE_FROM.DATABASE,
                        dbTrackList
                );
            }
        });
    }

    private static Task<TrackListResult> getServerTracksAsync() {
        Log.d("TAG", "loading tracks from server..");
        final TaskCompletionSource<TrackListResult> tcs = new TaskCompletionSource<>();
        ApiClient.getInstance().getTracks(new Callback<TrackResponse>() {
            @Override
            public void success(TrackResponse response) {
                TrackListResult result = new TrackListResult(
                        TrackListResult.TYPE_FROM.SERVER,
                        response.getTracks()
                );
                Log.d("TAG", "loading tracks from server: SUCCESS");
                tcs.setResult(result);
            }

            @Override
            public void failure(NetworkException exception) {
                Log.d("TAG", "loading tracks from server: ERROR "
                        + exception.getErrorCode());
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }

    private static Task<Void> refreshDbAsync(List<Track> serverTracks, List<Track> dbTracks) {
        serverTracks.removeAll(dbTracks);
        Log.e("TAG", "tack for inserting" + serverTracks);
        DbCrudHelper.insertTracks(serverTracks);
        Log.d("TAG", "refreshing DB tracks: SUCCESS");
        return null;
    }
}
