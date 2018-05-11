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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.receivers.AlarmReceiver;
import com.paranoid.runordie.utils.DateConverter;

import java.text.DateFormat;
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

    public static void createNotification(String title) {
        NotificationManager notificationManager = (NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

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

        Notification notification = new NotificationCompat.Builder(App.getInstance(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_exit_to_app_black_24dp)
                .setContentTitle("Run od DIe")
                .setContentText(title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(111, notification);
    }

    public static void createAlarmNotification(long execTime, String title) {
         Context context = App.getInstance();
         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

         PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0, // requestCode, полезно, если вызывается активити, или один ресивер обрабатывает несколько задач
                new Intent(context, AlarmReceiver.class)
                     .putExtra("TITLE_KEY", title),
                PendingIntent.FLAG_UPDATE_CURRENT // если такой PendingIntent уже есть, то заменить его
        );

        Log.e("TAG", "execTime = " + DateConverter.parseDateToString(execTime) + "||"+ DateConverter.parseTimeToString(execTime));

        Calendar calendar = Calendar.getInstance();
        Calendar calTest = Calendar.getInstance();
        calTest.setTimeInMillis(execTime - calendar.getTimeInMillis());
        Log.e("TAG", "execution time in " + calTest.get(Calendar.MINUTE) + "min, " + calTest.get(Calendar.SECOND) + "sec");
        Log.e("TAG", "time ms = "+ (execTime - calendar.getTimeInMillis()));

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, // время в UTC, если телефон "спит", то будет "разбужен""
                    execTime, // время, когда надо вызвать PendingIntent
                    pendingIntent // PendingIntent, который надо вызвать
            );
        } else if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP, // время в UTC, если телефон "спит", то будет "разбужен""
                    execTime, // время, когда надо вызвать PendingIntent
                    pendingIntent // PendingIntent, который надо вызвать
            );
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP, // время в UTC, если телефон "спит", то будет "разбужен""
                    execTime, // время, когда надо вызвать PendingIntent
                    pendingIntent // PendingIntent, который надо вызвать
            );
        }

    }

}
