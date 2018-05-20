package com.paranoid.runordie.server;

import android.util.Log;

import com.paranoid.runordie.models.httpResponses.AbstractResponse;

public class NetworkException extends RuntimeException {

    private static final String DEFAULT_ERROR_CODE = "NETWORK_ERROR";
    private final String errorCode;

    protected NetworkException(AbstractResponse response) {
        errorCode = response.getCode();
    }

    protected NetworkException(Throwable t) {
        errorCode = DEFAULT_ERROR_CODE;
        Log.e("TAG", "Network Exception: " + t.getMessage());
    }

    public String getErrorCode() {
        return errorCode == null ? DEFAULT_ERROR_CODE : errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode == null ? DEFAULT_ERROR_CODE : errorCode;
    }
}
