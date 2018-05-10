package com.paranoid.runordie.server.services;

import com.google.gson.annotations.JsonAdapter;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.models.httpResponses.PointsResponse;
import com.paranoid.runordie.models.httpResponses.RegisterResponse;
import com.paranoid.runordie.models.httpResponses.SaveTrackResponse;
import com.paranoid.runordie.models.httpResponses.TrackResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServerService {

    @POST("/senla-training-addition/lesson-26.php?method=login")
    Call<LoginResponse> login(@Body User user);

    @POST("/senla-training-addition/lesson-26.php?method=register")
    Call<RegisterResponse> register(@Body User user);

    @POST("/senla-training-addition/lesson-26.php?method=tracks")
    Call<TrackResponse> getTracks();

    @POST("/senla-training-addition/lesson-26.php?method=save")
    Call<SaveTrackResponse> saveTrack(@Body Track track);

    @POST("/senla-training-addition/lesson-26.php?method=points")
    Call<PointsResponse> getTrackPoints(@Body Track track);
}
