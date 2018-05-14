package com.paranoid.runordie.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.models.Notification;
import com.paranoid.runordie.receivers.AlarmReceiver;

import java.util.Calendar;

public class NotificationUtils {

    public static void createAlarmNotification(Notification notification) {
        String title = notification.getTitle();
        long execTime = notification.getExecutionTime();

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
