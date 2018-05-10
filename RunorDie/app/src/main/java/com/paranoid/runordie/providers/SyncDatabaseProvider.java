package com.paranoid.runordie.providers;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.TrackListResult;
import com.paranoid.runordie.models.httpResponses.PointsResponse;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

public class SyncDatabaseProvider {

    protected static void syncDBWithServer() {
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Log.d("TAG", "database synchronization with server..");
                return syncDBWithServerAsync().getResult();
            }
        });
    }

    private static Task<Void> syncDBWithServerAsync() {
        return Task.whenAllResult(loadAllTracks()).onSuccessTask(new Continuation<List<TrackListResult>, Task<Void>>() {
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
                    Log.d("TAG", "database synchronization with server: OK");
                    HomeBroadcast.sendBroadcast(HomeBroadcast.ACTION.TRACKS_REFRESHED);
                }
                if (task.isFaulted()) {
                    Log.e("TAG", "database synchronization with server: ERROR "
                            + task.getError().getMessage());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }


    private static List<Task<TrackListResult>> loadAllTracks() {
        List<Task<TrackListResult>> tasks = new ArrayList<>();
        Task<TrackListResult> serverLoadTask = getServerTracksAsync();
        Task<TrackListResult> dbLoadTask = getDbTrackAsync();
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
                Log.e("TAG", "loading tracks from server: ERROR "
                        + exception.getErrorCode());
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }

    private static Task<TrackListResult> getDbTrackAsync() {
        return Task.call(new Callable<TrackListResult>() {
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

    private static Task<Void> refreshDbAsync(List<Track> serverTracks, List<Track> dbTracks) {
        serverTracks.removeAll(dbTracks);
        List<Task<Void>> trackInsertTasks = new ArrayList<>();
        for (Track track : serverTracks) {
            trackInsertTasks.add(refreshTrack(track));
        }
        return Task.whenAll(trackInsertTasks);
    }

    private static Task<Void> refreshTrack(final Track track) {
        return getTrackPoints(track.getServerId()).onSuccessTask(new Continuation<List<LatLng>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<LatLng>> task) throws Exception {
                track.setPoints(task.getResult());
                return insertTrackIntoDb(track);
            }
        });
    }

    private static Task<List<LatLng>> getTrackPoints(final long serverId) {
        Log.d("TAG", "loading track points (id = " + serverId + ") from server..");
        final TaskCompletionSource<List<LatLng>> tcs = new TaskCompletionSource<>();
        ApiClient.getInstance().getTrackPoints(serverId, new Callback<PointsResponse>() {
            @Override
            public void success(PointsResponse response) {
                Log.d("TAG", "loading track points (id = " + serverId + ") from server: OK");
                tcs.setResult(response.getPoints());
            }

            @Override
            public void failure(NetworkException exception) {
                Log.e("TAG", "loading track points (id = " + serverId + ") from server: ERROR "
                        + exception.getErrorCode());
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }

    private static Task<Void> insertTrackIntoDb(final Track track) {
        return Task.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                DbCrudHelper.insertTrackWithServerId(track);
                Log.d("TAG", "refreshing track id = " + track.getServerId() + "OK");
                return null;
            }
        });
    }
}