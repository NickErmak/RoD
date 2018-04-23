package com.paranoid.runordie.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.paranoid.runordie.App;


public class BroadcastUtils {

    public static final String BROADCAST_ACTION = "local:BroadcastUtils.BROADCAST_ACTION";
    public static final String EXTRA_ACTION = "EXTRA_ACTION";

    public enum ACTION {SUCCESS_TRACK_UPDATE, ERROR_TRACK_UPDATE}

    public static void sendBroadcast(ACTION action) {
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(
                new Intent(BROADCAST_ACTION)
                        .putExtra(BroadcastUtils.EXTRA_ACTION, action)
        );
    }
}
