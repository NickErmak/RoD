package com.paranoid.runordie.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.paranoid.runordie.R;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.activities.SplashActivity;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.helpers.DetermineBestLocation;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.utils.DistanceUtils;
import com.paranoid.runordie.utils.PermissionUtils;

import java.util.LinkedList;
import java.util.List;


public class LocationService extends Service implements LocationListener {

    private final IBinder mBinder = new LocationBinder();

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    private static final int NOTIFICATION_ID = 1;
    private static final long TIME_BETWEEN_UPDATES = 5000;
    private static final float UPDATE_DISTANCE_THRESHOLD_METERS = 5.0f;

    private boolean isActive = true;
    private long startTime;
    private LocationManager locationManager;
    private Location bestLocation;
    private List<LatLng> points;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        isActive = false;
        points = new LinkedList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //first create
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTime = System.currentTimeMillis();


        if (!isActive && PermissionUtils.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            isActive = true;

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    TIME_BETWEEN_UPDATES,
                    UPDATE_DISTANCE_THRESHOLD_METERS,
                    this
            );

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
                            new Intent(this, RunActivity.class),
                            0
                    ))
                    .setContentTitle("Test TITLE")
                    .setContentText("Test TEXT")
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();

            startForeground(NOTIFICATION_ID, notification);
        }

        return Service.START_STICKY;
        //some component invoke startService
    }

    @Override
    public void onDestroy() {
        if (isActive) {
            isActive = false;
            saveTrack();
            locationManager.removeUpdates(this);
            stopForeground(true);
        }
        super.onDestroy();
    }

    private void saveTrack() {

        if (points.size() >= 2) {
            Track track = new Track(
                    startTime,
                    System.currentTimeMillis() - startTime,
                    DistanceUtils.getDistance(points),
                    points
            );
            Log.d("TAG", "insert new track in db");
            DbCrudHelper.insertTrackNoServerId(track);
        } else {
            Log.e("TAG", "can't save track: too little points");
        }
    }


    //TODO: main thread for JPS?!
    @Override
    public void onLocationChanged(Location location) {
        if (DetermineBestLocation.isBetterLocation(location, bestLocation)) {
            bestLocation = location;
            LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());
            points.add(newPoint);
            //TODO: write bestLocation to DB?
            Log.e("TAG", "New point: " + newPoint.toString()
                    + "Thread=  " + Thread.currentThread().getName());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
