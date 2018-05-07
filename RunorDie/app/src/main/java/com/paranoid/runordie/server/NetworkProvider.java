package com.paranoid.runordie.server;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;

import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.TrackListResult;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

public class NetworkProvider {

    /*  public static void loginAndRefresh(User user) {
          loginAndRefreshAsync(user).getResult();
      }

      private static Task<Void> loginAndRefreshAsync(User user) {
          return loginAsync(user).onSuccessTask(new Continuation<LoginResponse, Task<Void>>() {
              @Override
              public Task<Void> then(Task<LoginResponse> task) {
                  return refreshDbAsync();
              }
          });
      }*/
    private static List<Track> getCurrentTrackList(Cursor cursor) {
        List<Track> tracks = new LinkedList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int trackIdIndex = cursor.getColumnIndexOrThrow(Track._ID);
                int serverIdIndex = cursor.getColumnIndexOrThrow(Track.ID);

                do {
                    long serverId = cursor.getLong(serverIdIndex);
                    long _id = cursor.getLong(trackIdIndex);

                    Track track = new Track(serverId, _id);
                    tracks.add(track);
                } while (cursor.moveToNext());
            }
        }
        return tracks;
    }


    public static Task<TrackListResult> loaderAsync(Loader<Cursor> loader) {
        Log.d("TAG", "trying to load tracks from server");
        final TaskCompletionSource<TrackListResult> tcs = new TaskCompletionSource<>();


        loader.registerListener(11, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
                Log.e("TAG", "listener catched!");
                TrackListResult result = new TrackListResult(
                        TrackListResult.TYPE_FROM.DATABASE,
                        getCurrentTrackList(data)
                );
                tcs.setResult(result);
            }
        });
        return tcs.getTask();
    }


    public static Task<List<TrackListResult>> refreshDbAsync(final Loader<Cursor> trackLoader) {

        List<Task<TrackListResult>> tasks = new ArrayList<>();
        tasks.add(loaderAsync(trackLoader));
        tasks.add(getTracksAsync());

        return Task.whenAllResult(tasks);

    }

    /*public static Task<Void> refreshDbFirstLaunchAsync() {
        return getTracksAsync().onSuccessTask(new Continuation<TrackResponse, Task<Void>>() {
            @Override
            public Task<Void> then(Task<TrackResponse> task) {
                DbCrudHelper.loadTracks();
                return null;
            }
        }).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                HomeBroadcast.sendBroadcast(HomeBroadcast.ACTION.TRACKS_REFRESHED);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }*/


    static Task<TrackListResult> getTracksAsync() {
        Log.d("TAG", "trying to load tracks from server");
        final TaskCompletionSource<TrackListResult> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().getTracks(new Callback<TrackResponse>() {
            @Override
            public void success(TrackResponse response) {
                TrackListResult result = new TrackListResult(
                        TrackListResult.TYPE_FROM.SERVER,
                        response.getTracks()
                        );
                tcs.setResult(result);
                Log.d("TAG", "success track loading");
            }

            @Override
            public void failure(NetworkException exception) {
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }
}
