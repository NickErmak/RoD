package com.paranoid.runordie.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.paranoid.runordie.App;

public class PreferenceUtils {

    public static final String DEFAULT_VALUE = "";
    private static final String LOGIN_KEY = "LOGIN_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor editor;

    static {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        editor = sPref.edit();
    }

    public static String getLogin() {
        return getPref(LOGIN_KEY, DEFAULT_VALUE);
    }

    public static void setLogin(String loginValue) {
        setPref(LOGIN_KEY, loginValue);
    }

    public static String getPassword() {
        return getSecurePref(PASSWORD_KEY, DEFAULT_VALUE);
    }

    public static void setPassword(String passwordValue) {
        setSecurePref(PASSWORD_KEY, passwordValue);
    }

    private static String getSecurePref(String prefKey, String defValue) {
        return SecurityUtils.decode(getPref(prefKey, defValue));
    }

    private static String getPref(String prefKey, String defValue) {
        return sPref.getString(prefKey, defValue);
    }

    private static void setSecurePref(String prefKey, String prefValue) {
        setPref(prefKey, SecurityUtils.encode(prefValue));
    }

    private static void setPref(String prefKey, String prefValue) {
        editor.putString(prefKey, prefValue)
                .commit();
    }
}
