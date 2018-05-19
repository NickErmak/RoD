package com.paranoid.runordie.utils;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.paranoid.runordie.App;

public class RingtoneUtils {

    public static void playRingtone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(App.getInstance(), notification);
        r.play();
    }
}
