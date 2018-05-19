package com.paranoid.runordie.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "228";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    private static final String NOTIFICATION_TITLE = App.getInstance().getString(R.string.notification_content_title);
    private static final int NOTIFICATION_ID = 882;
    private static final int PENDING_INTENT_REQUEST_CODE = 115;

    public static void createChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(String msg, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) App.getInstance()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            createChannel(notificationManager);
            Notification newNotification = createNotification(msg, intent);
            notificationManager.notify(
                    NOTIFICATION_ID,
                    newNotification
            );
        } else {
            Log.e("TAG", "Can't get notification service");
            SnackbarUtils.showSnack(R.string.default_error);
        }
    }


    public static Notification createForegroundNotification(String msg, Intent intent) {
        NotificationCompat.Builder builder = (intent != null) ? newNotificationBuilderWithIntent(msg, intent)
                : newNotificationBuilder(msg);

        return builder.setAutoCancel(false)
                .setOngoing(true)
                .build();
    }

    private static Notification createNotification(String msg, Intent intent) {
        NotificationCompat.Builder builder = (intent != null) ? newNotificationBuilderWithIntent(msg, intent)
                : newNotificationBuilder(msg);
        return builder.setAutoCancel(true)
                .build();
    }

    private static NotificationCompat.Builder newNotificationBuilderWithIntent(String msg, Intent intent) {
        PendingIntent pendingIntent =
                TaskStackBuilder.create(App.getInstance())
                        .addNextIntentWithParentStack(intent)
                        .getPendingIntent(PENDING_INTENT_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        return newNotificationBuilder(msg)
                .setContentIntent(pendingIntent);
    }

    private static NotificationCompat.Builder newNotificationBuilder(String msg) {
        return new NotificationCompat.Builder(App.getInstance(), CHANNEL_ID)
                .setSmallIcon(R.drawable.main_icon)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
