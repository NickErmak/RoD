package com.paranoid.runordie.utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.paranoid.runordie.dialogs.PermissionDialog;

public class PermissionUtils {

    public static final int MY_PERMISSIONS_REQUEST = 123;

    public static void requestPermission(Activity activity, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission)) {
            Log.e("TAG", "shouldShowRequestPermissionRationale = true");
            PermissionDialog.newInstance(permission).show(activity.getFragmentManager(), null);

        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "checkSelf permission = true");
                return false;
            }
        }
        return true;
    }
}
