package com.paranoid.runordie.server.interceptors;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.models.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
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
        Session session = App.getInstance().getState().getActiveSession();

        if (session != null) {
            RequestBody requestBody = request.body();
            MediaType contentType = requestBody.contentType();
            requestBody = putTokenToBody(session.getToken(), requestBody, contentType);
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
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return e.getMessage();
        }
    }
}
