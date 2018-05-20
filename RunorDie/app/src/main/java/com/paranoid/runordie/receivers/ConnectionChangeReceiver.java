package com.paranoid.runordie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.paranoid.runordie.R;
import com.paranoid.runordie.utils.ConnectionUtils;
import com.paranoid.runordie.utils.SnackbarUtils;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectionUtils.checkInternetConnection()) {
            SnackbarUtils.showSnack(R.string.internet_connection_error);
        }
        Log.d("TAG", "Internet connection: " + ConnectionUtils.checkInternetConnection());
    }
}
