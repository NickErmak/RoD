package com.paranoid.runordie.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.paranoid.runordie.App;


public class BroadcastUtils {

    public static final String BROADCAST_ACTION = "local:BroadcastUtils.BROADCAST_ACTION";
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_ERROR = "EXTRA_ERROR";

    public enum ACTION {SUCCESS_LOGIN, ERROR}

    //public enum ERROR_CODE {INVALID_TOKEN}

    public static void sendBroadcast(ACTION action) {
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(
                new Intent(BROADCAST_ACTION)
                        .putExtra(BroadcastUtils.EXTRA_ACTION, action)
        );
    }

    public static void sendBroadcast(ACTION action, String error) {
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(
                new Intent(BROADCAST_ACTION)
                        .putExtra(BroadcastUtils.EXTRA_ACTION, action)
                        .putExtra(BroadcastUtils.EXTRA_ERROR, error)
        );
    }
}
