package com.paranoid.runordie.utils;

import com.paranoid.runordie.App;
import com.paranoid.runordie.helpers.PreferenceHelper;
import com.paranoid.runordie.models.Session;
import com.paranoid.runordie.models.User;

public class SessionUtils {

    public static void createSession(User user, String token) {
        PreferenceHelper.saveUser(user);
        Session activeSession = new Session(user, token);
        App.getInstance().getState().setActiveSession(activeSession);
    }
}
