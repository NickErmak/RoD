package com.paranoid.runordie.utils.broadcastUtils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.paranoid.runordie.App;

public class NotificationBroadcast {

    public static final String BROADCAST_ACTION = "local:NotificationBroadcast.BROADCAST_ACTION";
    public static final String EXTRA_ACTION = "EXTRA_ACTION";

    public enum ACTION {REFRESHING_DB_SUCCESS}

    public static void sendBroadcast(ACTION action) {
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(
                new Intent(BROADCAST_ACTION)
                        .putExtra(EXTRA_ACTION, action)
        );
    }
}
