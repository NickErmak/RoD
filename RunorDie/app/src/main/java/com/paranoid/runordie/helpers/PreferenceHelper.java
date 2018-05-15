package com.paranoid.runordie.helpers;

import android.util.Log;

import com.paranoid.runordie.models.User;
import com.paranoid.runordie.utils.PreferenceUtils;
import com.paranoid.runordie.utils.SecurityUtils;

public class PreferenceHelper {

    private static final String LOGIN_KEY = "LOGIN_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";
    private static final String FIRST_NAME_KEY = "FIRST_NAME_KEY";
    private static final String LAST_NAME_KEY = "LAST_NAME_KEY";
    private static final String FIRST_LAUNCH_KEY = "FIRST_LAUNCH_KEY";

    public static User loadUser() {
        User user = null;
        String email = PreferenceUtils.getStringPref(LOGIN_KEY);
        String password = SecurityUtils.decode(
                PreferenceUtils.getStringPref(PASSWORD_KEY)
        );

        if (!(email == null || password == null)) {
            Log.d("TAG", "active session");
            user = new User(email, password);
        } else {
            Log.d("TAG", "guest session");
        }
        return user;
    }

    public static void saveUser(User user) {
        PreferenceUtils.setStringPref(LOGIN_KEY, user.getEmail());
        PreferenceUtils.setStringPref(FIRST_NAME_KEY, user.getFirstName());
        PreferenceUtils.setStringPref(LAST_NAME_KEY, user.getLastName());
        PreferenceUtils.setStringPref(
                PASSWORD_KEY,
                SecurityUtils.encode(user.getPassword())
        );
    }

    public static boolean isFirstLaunch() {
        return PreferenceUtils.getBooleanPref(FIRST_LAUNCH_KEY, true);
    }

    public static void executeFirstLaunch() {
        PreferenceUtils.setBooleanPref(FIRST_LAUNCH_KEY, false);
    }
}
