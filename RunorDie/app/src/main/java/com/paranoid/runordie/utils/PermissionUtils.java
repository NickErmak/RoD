package com.paranoid.runordie.utils;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.paranoid.runordie.App;
import com.paranoid.runordie.dialogs.PermissionDialog;

public class PermissionUtils {

    public static final int MY_PERMISSIONS_REQUEST = 123;

    public static void requestPermission(Activity activity, String permission, String permissionTitle) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission)) {
            PermissionDialog.newInstance(permission, permissionTitle).show(activity.getFragmentManager(), null);

        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST
            );
        }
    }

    public static boolean checkPermission(String permission) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(App.getInstance(), permission) == PackageManager.PERMISSION_GRANTED;
    }
}
