package com.paranoid.runordie.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.activities.SplashActivity;

public class LocationService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private boolean isFirstLaunch = true;

    @Override
    public void onCreate() {
        super.onCreate();
        //first create
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //some component want to bind to this service
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (isFirstLaunch) {
            String CHANNEL_ID = "3000";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_exit_to_app_black_24dp)
                    .setContentIntent(PendingIntent.getActivity(
                    this,
                    0,
                    new Intent(this, SplashActivity.class),
                    0
            ))
                    .setContentTitle("Test TITLE")
                    .setContentText("Test TEXT")
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();

            startForeground(NOTIFICATION_ID, notification);
            isFirstLaunch = false;
        }

        return Service.START_STICKY;
        //some component invoke startService
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
