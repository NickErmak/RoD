package com.paranoid.runordie.server;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paranoid.runordie.adapters.type.DateTypeAdapter;
import com.paranoid.runordie.adapters.type.LatLngTypeAdapter;
import com.paranoid.runordie.server.interceptors.AuthInterceptor;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    protected static Retrofit retrofitClient;

    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(LatLng.class, new LatLngTypeAdapter())
                .create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        retrofitClient = new Retrofit.Builder()
                .baseUrl("http://pub.zame-dev.org/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }
}
