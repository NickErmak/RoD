package com.paranoid.runordie.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.utils.PermissionUtils;
import com.paranoid.runordie.utils.SnackbarUtils;

public class LocationHelper {

    public static final int LOCATION_REQUEST_CODE = 321;
    private static final String ACCESS_FINE_LOCATION_TITLE;
    private static final String LOCATION_ACCESS_REQUEST;
    private static final String POSITIVE_BUTTON_TEXT;
    private static final String NEGATIVE_BUTTON_TEXT;
    private static final String LOCATION_ACCESS_DENIED;

    static {
        Context context = App.getInstance();
        ACCESS_FINE_LOCATION_TITLE = context.getString(R.string.permission_access_fine_location);
        LOCATION_ACCESS_REQUEST = context.getString(R.string.location_access_request);
        POSITIVE_BUTTON_TEXT = context.getString(R.string.ok);
        NEGATIVE_BUTTON_TEXT = context.getString(R.string.no);
        LOCATION_ACCESS_DENIED = context.getString(R.string.location_access_denied);
    }

    public static boolean checkLocationPermission() {
        boolean isGranted = PermissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (!isGranted) {
            requestLocationPermission();
        }
        return isGranted;
    }

    public static boolean checkLocationAccess() {
        Context context = App.getInstance();
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isJpsEnabled = false;

        try {
            isJpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception exception) {
            Log.e("TAG", "Error: " + exception.getMessage());
        }

        if (!isJpsEnabled) {
            requestLocationAccess();
        }
        return isJpsEnabled;
    }

    private static void requestLocationPermission() {
        Activity currentActivity = App.getInstance().getState().getCurrentActivity();
        if (currentActivity != null) {
            PermissionUtils.requestPermission(
                    currentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ACCESS_FINE_LOCATION_TITLE
            );
        }
    }

    private static void requestLocationAccess() {
        final Activity currentActivity = App.getInstance().getState().getCurrentActivity();
        if (currentActivity != null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(currentActivity);
            dialog.setMessage(LOCATION_ACCESS_REQUEST);
            dialog.setPositiveButton(POSITIVE_BUTTON_TEXT, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    currentActivity.startActivityForResult(myIntent, LOCATION_REQUEST_CODE);
                }
            });
            dialog.setNegativeButton(NEGATIVE_BUTTON_TEXT, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    SnackbarUtils.showSnack(LOCATION_ACCESS_DENIED);
                }
            });
            dialog.show();
        }
    }
}
