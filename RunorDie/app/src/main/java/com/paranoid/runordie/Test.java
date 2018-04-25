package com.paranoid.runordie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.receivers.AlarmReceiver;

import java.util.Calendar;

public class Test {

    static String insertTrack =  "INSERT INTO track (startTime, distance, runTime, points) VALUES (?, ?, ?, ?)";
    static String insertNotification =  "INSERT INTO notification (executionTime, title) VALUES (?, ?)";

    public static void fillDB() {
        DbCrudHelper.insert(
                insertNotification,
                new String[] {"1265163", "title1"}
                );

        DbCrudHelper.insert(
                insertNotification,
                new String[] {"123", "title2"}
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

    public static void createAlarmNotification() {
        Context context = App.getInstance();
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

         PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0, // requestCode, полезно, если вызывается активити, или один ресивер обрабатывает несколько задач
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT // если такой PendingIntent уже есть, то заменить его
        );
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP, // время в UTC, если телефон "спит", то будет "разбужен""
                calendar.getTimeInMillis(), // время, когда надо вызвать PendingIntent
                pendingIntent // PendingIntent, который надо вызвать
        );
    }
}
