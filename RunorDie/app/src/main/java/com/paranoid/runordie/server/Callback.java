package com.paranoid.runordie.server;

import android.support.annotation.NonNull;
import android.util.Log;

import com.paranoid.runordie.models.httpResponses.AbstractResponse;

import retrofit2.Call;
import retrofit2.Response;

public abstract class Callback<T extends AbstractResponse> implements retrofit2.Callback<T> {

    @Override
    public final void onResponse(@NonNull Call<T> call, Response<T> response) {
        switch (response.body().getStatus()) {
            case ok:
                success(response.body());
                break;
            case error:
                failure(new NetworkException(response.body()));
                break;
        }
    }

    @Override
    public final void onFailure(@NonNull Call<T> call, Throwable t) {
        failure(new NetworkException(t));
    }

    public abstract void success(T result);

    public abstract void failure(NetworkException exception);
}
