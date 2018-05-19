package com.paranoid.runordie.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Notification;
import com.paranoid.runordie.receivers.AlarmReceiver;

public class AlarmManagerUtils {

    public static final String NOTIFICATION_MSG_KEY = "NOTIFICATION_MSG_KEY";
    private static final int PENDING_INTENT_REQUEST_CODE = 111;

    public static void setAlarm(Notification notification){
        String title = notification.getTitle();
        long execTime = notification.getExecutionTime();

        Context context = App.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Log.e("TAG", "Can't get alarm service");
            SnackbarUtils.showSnack(R.string.default_error);
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                PENDING_INTENT_REQUEST_CODE,
                new Intent(context, AlarmReceiver.class)
                        .putExtra(NOTIFICATION_MSG_KEY, title),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    execTime,
                    pendingIntent
            );
        } else if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    execTime,
                    pendingIntent
            );
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    execTime,
                    pendingIntent
            );
        }
    }

    public static void deleteAlarm() {
        Context context = App.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Log.e("TAG", "Can't get alarm service");
            SnackbarUtils.showSnack(R.string.default_error);
            return;
        }

        Intent cancelIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                PENDING_INTENT_REQUEST_CODE,
                cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
