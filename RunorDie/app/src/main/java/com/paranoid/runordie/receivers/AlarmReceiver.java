package com.paranoid.runordie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.Test;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.activities.SplashActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        PowerManager pm = (PowerManager) App.getInstance().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE,
                "tag");
        wl.acquire(0);
        Test.createNotification(intent.getStringExtra("TITLE_KEY"));

        wl.release();



        Log.e("TAG", "receive alarm");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(App.getInstance(), notification);
        r.play();

        App.getInstance().startActivity(new Intent(App.getInstance(), SplashActivity.class));
    }
}
