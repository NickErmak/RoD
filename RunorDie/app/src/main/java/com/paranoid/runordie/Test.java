package com.paranoid.runordie;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.paranoid.runordie.helpers.DbCrudHelper;

public class Test {

    static String insertTrack =  "INSERT INTO track (startTime, distance, runTime, points) VALUES (?, ?, ?, ?)";

    public static void fillDB() {
        DbCrudHelper.insert(
                insertTrack,
                new String[] {"123", "123", "100", ""}
                );

        DbCrudHelper.insert(
                insertTrack,
                new String[] {"566", "905", "214", ""}
        );
    }

    public static void createNotification(Activity activity) {
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "3000";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "channel title";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String chanel_id = "3000";

            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_exit_to_app_black_24dp)
                .setContentTitle("Test TITLE")
                .setContentText("Test TEXT")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(111, notification);
    }
}
