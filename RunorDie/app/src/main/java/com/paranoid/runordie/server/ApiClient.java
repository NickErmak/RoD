package com.paranoid.runordie.server;

import android.util.Log;

import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.models.httpResponses.PointsResponse;
import com.paranoid.runordie.models.httpResponses.RegisterResponse;
import com.paranoid.runordie.models.httpResponses.SaveTrackResponse;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.server.services.ServerService;

//TODO: CATCH ALL ERRORS
public class ApiClient {

    private static volatile ApiClient instance;
    private ServerService serverServiceInstance;

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    private ApiClient() {
        serverServiceInstance = NetworkUtils.retrofitClient.create(ServerService.class);
    }

    public void login(User user, final Callback<LoginResponse> callback) {
        serverServiceInstance.login(user).enqueue(callback);
    }

    public void register(User user, final Callback<RegisterResponse> callback) {
        serverServiceInstance.register(user).enqueue(callback);
    }

    public void getTracks(final Callback<TrackResponse> callback) {
        serverServiceInstance.getTracks().enqueue(callback);
    }

    public void getTrackPoints(long trackId, final Callback<PointsResponse> callback) {
        serverServiceInstance.getTrackPoints(trackId).enqueue(callback);
    }

    public void saveTrack(Track track, final Callback<SaveTrackResponse> callback) {
        serverServiceInstance.saveTrack(track).enqueue(callback);
    }
}
