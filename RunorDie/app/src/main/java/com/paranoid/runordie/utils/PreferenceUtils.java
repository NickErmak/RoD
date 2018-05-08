package com.paranoid.runordie.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.paranoid.runordie.App;

public class PreferenceUtils {

    private static final String LOGIN_KEY = "LOGIN_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";
    private static final String FIRST_LAUNCH_KEY = "FIRST_LAUNCH_KEY";

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor editor;

    static {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        editor = sPref.edit();
    }

    public static boolean isFirstLaunch() {
        return sPref.getBoolean(FIRST_LAUNCH_KEY, true);
    }

    public static void executeFirstLaunch() {
        editor.putBoolean(FIRST_LAUNCH_KEY, false).apply();
    }

    public static String getLogin() {
        return getPref(LOGIN_KEY);
    }

    public static void setLogin(String loginValue) {
        setPref(LOGIN_KEY, loginValue);
    }

    public static String getPassword() {
        return getSecurePref(PASSWORD_KEY);
    }

    public static void setPassword(String passwordValue) {
        setSecurePref(PASSWORD_KEY, passwordValue);
    }

    private static String getSecurePref(String prefKey) {
        String pref = getPref(prefKey);
        return pref != null ? SecurityUtils.decode(pref) : null;
    }

    private static String getPref(String prefKey) {
        return sPref.getString(prefKey, null);
    }


    private static void setSecurePref(String prefKey, String prefValue) {
        setPref(prefKey, SecurityUtils.encode(prefValue));
    }

    private static void setPref(String prefKey, String prefValue) {
        editor.putString(prefKey, prefValue)
                .apply();
    }
}
