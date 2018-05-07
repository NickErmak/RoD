package com.paranoid.runordie.helpers;

import android.util.Log;

import com.paranoid.runordie.models.User;
import com.paranoid.runordie.utils.PreferenceUtils;

public class PreferenceHelper {

    public static User loadUserData() {
        User user = null;
        String email = PreferenceUtils.getLogin();
        String password = PreferenceUtils.getPassword();
        if (!(email == null || password == null)) {
            Log.d("TAG", "active session");
            user = new User(email, password);
        } else {
            Log.d("TAG", "guest session");
        }
        return user;
    }
}
