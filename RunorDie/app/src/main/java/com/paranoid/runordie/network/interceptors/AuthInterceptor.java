package com.paranoid.runordie.network.interceptors;

import com.paranoid.runordie.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class AuthInterceptor implements Interceptor {

    private static final String TOKEN = "token";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String token = App.getInstance().getState().getToken();

        if (token != null) {
            RequestBody requestBody = request.body();
            MediaType contentType = requestBody.contentType();
            requestBody = putTokenToBody(token, requestBody, contentType);
            request = request.newBuilder()
                    .post(requestBody)
                    .build();
        }
        return chain.proceed(request);
    }

    private RequestBody putTokenToBody(String token, RequestBody requestBody, MediaType contentType) {
        try {
            JSONObject obj = (contentType == null) ? new JSONObject()
                    : new JSONObject(bodyToString(requestBody));
            obj.put(TOKEN, token);
            return RequestBody.create(requestBody.contentType(), obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return e.getMessage();
        }
    }
}
