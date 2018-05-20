package com.paranoid.runordie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.activities.SplashActivity;
import com.paranoid.runordie.utils.AlarmManagerUtils;
import com.paranoid.runordie.utils.NotificationUtils;
import com.paranoid.runordie.utils.RingtoneUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "Alarm receiver: onReceive");

        Intent routeIntent = new Intent(App.getInstance(), SplashActivity.class);
        routeIntent.putExtra(
                SplashActivity.ROUTE_KEY,
                SplashActivity.ROUTE.RUN_ACTIVITY
        );

        NotificationUtils.showNotification(
                intent.getStringExtra(AlarmManagerUtils.NOTIFICATION_MSG_KEY),
                routeIntent
        );
        RingtoneUtils.playRingtone();
    }
}
