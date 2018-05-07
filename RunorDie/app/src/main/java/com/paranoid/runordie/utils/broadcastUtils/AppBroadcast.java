package com.paranoid.runordie.utils.broadcastUtils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.paranoid.runordie.App;


public class AppBroadcast {

    public static final String BROADCAST_ACTION = "local:AppBroadcast.BROADCAST_ACTION";

    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_ERROR = "EXTRA_ERROR";

    public enum ACTION {SUCCESS_LOGIN, FAIL_LOGIN}

    //public enum ERROR_CODE {INVALID_TOKEN}

    public static void sendBroadcast(ACTION action) {
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(
                new Intent(BROADCAST_ACTION)
                        .putExtra(EXTRA_ACTION, action)
        );
    }

    public static void sendBroadcast(ACTION action, String error) {
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(
                new Intent(BROADCAST_ACTION)
                        .putExtra(EXTRA_ACTION, action)
                        .putExtra(EXTRA_ERROR, error)
        );
    }
}
