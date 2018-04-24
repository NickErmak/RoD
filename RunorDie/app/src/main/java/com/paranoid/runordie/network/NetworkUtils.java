package com.paranoid.runordie.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paranoid.runordie.App;
import com.paranoid.runordie.adapters.DateTypeAdapter;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.models.PointD;
import com.paranoid.runordie.models.httpResponses.PointsResponse;
import com.paranoid.runordie.models.httpResponses.RegisterResponse;
import com.paranoid.runordie.models.httpResponses.SaveTrackResponse;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.network.interceptors.AuthInterceptor;

import com.paranoid.runordie.network.services.ServerService;
import com.paranoid.runordie.utils.BroadcastUtils;
import com.paranoid.runordie.utils.PreferenceUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static ServerService serverServiceInstance;
    private static OkHttpClient okHttpClient;

    //TODO: CATCH ALL ERRORS
    //adding interceptor for okHttp client
    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pub.zame-dev.org/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build();
        serverServiceInstance = retrofit.create(ServerService.class);
    }

    public static void login(final String email, final String password) {
       // User user = new User("atory29@yandex.ru", "k11112222K");
        User user = new User(email, password);
        serverServiceInstance.login(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                switch (response.body().getStatus()) {
                    case ok:
                        Log.d("TAG", "success login");
                        BroadcastUtils.sendBroadcast(BroadcastUtils.ACTION.SUCCESS_LOGIN);
                        App.getInstance().getState().setToken(response.body().getToken());
                        break;
                    case error:
                        Log.e("TAG", "response = " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("TAG", "fail = " + t.getMessage());
            }
        });
    }

    public static void register() {
        User user = new User("atory29@yandex.ru", "Johnnie", "Walker", "k11112222K");
        serverServiceInstance.register(user).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.e("TAG", "response = " + response.body().toString());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("TAG", "fail = " + t.getMessage());
            }
        });
    }

    public static void getTracks() {
        serverServiceInstance.getTracks().enqueue(new Callback<TrackResponse>() {
            @Override
            public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
                switch (response.body().getStatus()) {

                    case ok:
                        Log.e("TAG", "ok");
                        break;
                    case error:



                        BroadcastUtils.sendBroadcast(
                                BroadcastUtils.ACTION.ERROR,
                                response.body().getCode()
                                );
                        Log.e("TAG", "fail");
                        break;
                }
                List<Track> tracks = response.body().getTracks();
                if (tracks != null) {
                    Log.d("TAG", "response = " + response.body().getTracks().toString());
                } else {
                    Log.d("TAG", "no current tracks");
                }
            }

            @Override
            public void onFailure(Call<TrackResponse> call, Throwable t) {
                Log.d("TAG", "fail = " + t.getMessage());

            }
        });
    }

    public static void getTrackPoints() {
        serverServiceInstance.getTrackPoints(new Track(358L)).enqueue(new Callback<PointsResponse>() {
            @Override
            public void onResponse(Call<PointsResponse> call, Response<PointsResponse> response) {
                Log.e("TAG", "response = " + response.body().getPoints());
            }

            @Override
            public void onFailure(Call<PointsResponse> call, Throwable t) {
                Log.e("TAG", "fail = " + t.getMessage());
            }
        });

    }

    public static void saveTrack(Track track) {
        List<PointD> points = new LinkedList<>();
        points.add(new PointD(1, 1));
        points.add(new PointD(3, 1));

        serverServiceInstance.saveTrack(track).enqueue(new Callback<SaveTrackResponse>() {
            @Override
            public void onResponse(Call<SaveTrackResponse> call, Response<SaveTrackResponse> response) {
                Log.e("TAG", "response = " + response.body().getId());
            }

            @Override
            public void onFailure(Call<SaveTrackResponse> call, Throwable t) {
                Log.e("TAG", "fail = " + t.getMessage());
            }
        });
    }
}
