package com.paranoid.runordie.server;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.models.Session;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.models.httpResponses.PointsResponse;
import com.paranoid.runordie.models.httpResponses.RegisterResponse;
import com.paranoid.runordie.models.httpResponses.SaveTrackResponse;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.utils.broadcastUtils.AppBroadcast;

import bolts.Task;
import bolts.TaskCompletionSource;

public class NetworkProviderTasks {

    public static Task<RegisterResponse> registerAsync(final User user) {
        Log.d("TAG", "trying to register");
        final TaskCompletionSource<RegisterResponse> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().register(user, new Callback<RegisterResponse>() {
            @Override
            public void success(RegisterResponse result) {
                tcs.setResult(result);
                Log.d("TAG", "success register");
                Session activeSession = new Session(user, result.getToken());
                App.getInstance().getState().setActiveSession(activeSession);
            }

            @Override
            public void failure(NetworkException exception) {
                tcs.setError(exception);
                AppBroadcast.sendBroadcast(AppBroadcast.ACTION.FAIL_LOGIN);
            }
        });
        return tcs.getTask();
    }


    public static Task<LoginResponse> loginAsync(final User user) {
        Log.d("TAG", "trying to login");
        final TaskCompletionSource<LoginResponse> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().login(user, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse result) {
                tcs.setResult(result);
                Log.d("TAG", "success login");
                Session activeSession = new Session(user, result.getToken());
                App.getInstance().getState().setActiveSession(activeSession);
            }

            @Override
            public void failure(NetworkException exception) {
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }

    static Task<TrackResponse> getTracksAsync() {
        Log.d("TAG", "trying to load tracks from server");
        final TaskCompletionSource<TrackResponse> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().getTracks(new Callback<TrackResponse>() {
            @Override
            public void success(TrackResponse result) {
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

    static Task<SaveTrackResponse> saveTrackAsync(Track track) {
        Log.d("TAG", "trying to save track on server, id = " + track.getDbId());
        final TaskCompletionSource<SaveTrackResponse> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().saveTrack(track, new Callback<SaveTrackResponse>() {
            @Override
            public void success(SaveTrackResponse result) {
                tcs.setResult(result);
                Log.d("TAG", "success track saving");
            }

            @Override
            public void failure(NetworkException exception) {
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }

    static Task<PointsResponse> getTrackPointsAsync(long trackId) {
        Log.d("TAG", "trying to load track points from server, id = " + trackId);
        final TaskCompletionSource<PointsResponse> tcs = new TaskCompletionSource<>();

        ApiClient.getInstance().getTrackPoints(trackId, new Callback<PointsResponse>() {
            @Override
            public void success(PointsResponse result) {
                tcs.setResult(result);
                Log.d("TAG", "success track points loading");
            }

            @Override
            public void failure(NetworkException exception) {
                tcs.setError(exception);
            }
        });
        return tcs.getTask();
    }
}
