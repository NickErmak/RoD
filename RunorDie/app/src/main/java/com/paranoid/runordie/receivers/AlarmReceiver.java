package com.paranoid.runordie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.Test;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG", "receive alarm");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(App.getInstance(), notification);
        r.play();

        Test.createNotification(intent.getStringExtra("TITLE_KEY"));
    }
}
