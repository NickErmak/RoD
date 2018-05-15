package com.paranoid.runordie.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.paranoid.runordie.App;

public class PreferenceUtils {

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor editor;

    static {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        editor = sPref.edit();
    }

    public static String getStringPref(String prefKey) {
        return sPref.getString(prefKey, null);
    }

    public static boolean getBooleanPref(String prefKey, boolean defValue) {
        return sPref.getBoolean(prefKey, defValue);
    }

    public static void setStringPref(String prefKey, String prefValue) {
        editor.putString(prefKey, prefValue)
                .apply();
    }

    public static void setBooleanPref(String prefKey, boolean prefValue) {
        editor.putBoolean(prefKey, false)
                .apply();
    }
}
