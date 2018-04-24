package com.paranoid.runordie.utils;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.network.NetworkUtils;

public class SessionUtils {

    public static void initializeSession() {
        String email = PreferenceUtils.getLogin();
        String password = PreferenceUtils.getPassword();
        if (!email.equals(PreferenceUtils.DEFAULT_VALUE)) {
            NetworkUtils.login(email, password);
            Log.d("TAG", "active session");
        } else {
            Log.d("TAG", "non active session");
        }
    }
}
