package com.paranoid.runordie.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.paranoid.runordie.R;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.helpers.DetermineBestLocation;
import com.paranoid.runordie.helpers.LocationHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.providers.SaveTrackProvider;
import com.paranoid.runordie.utils.DistanceUtils;
import com.paranoid.runordie.utils.NotificationUtils;
import com.paranoid.runordie.utils.SnackbarUtils;

import java.util.LinkedList;
import java.util.List;

public class LocationService extends Service implements LocationListener {

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    private static final int NOTIFICATION_ID = 1;
    private static final long TIME_BETWEEN_UPDATES = 5000;
    private static final float UPDATE_DISTANCE_THRESHOLD_METERS = 5.0f;

    private final IBinder binder = new LocationBinder();
    private String notificationMsg;
    private String shortDistanceError;
    private int minTrackPoints;

    private boolean isActive = true;
    private long startTime;
    private LocationManager locationManager;
    private Location bestLocation;
    private List<LatLng> points;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isActive = false;
        startTime = System.currentTimeMillis();
        points = new LinkedList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        notificationMsg = getString(R.string.notification_track_writing_msg);
        minTrackPoints = getResources().getInteger(R.integer.min_track_points);
        shortDistanceError = getString(R.string.short_distance_error);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isActive && LocationHelper.checkLocationPermission()) {
            isActive = true;

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    TIME_BETWEEN_UPDATES,
                    UPDATE_DISTANCE_THRESHOLD_METERS,
                    this
            );

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationUtils.createChannel(notificationManager);
            startForeground(
                    NOTIFICATION_ID,
                    NotificationUtils.createForegroundNotification(
                            notificationMsg,
                            new Intent(this, RunActivity.class)
                    )
            );
        }
        return Service.START_STICKY;
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
        if (points.size() >= minTrackPoints) {
            Track track = new Track(
                    startTime,
                    System.currentTimeMillis() - startTime,
                    DistanceUtils.getDistance(points),
                    points
            );
            SaveTrackProvider.saveTrack(track);
        } else {
            Log.e("TAG", shortDistanceError);
            SnackbarUtils.showSnack(shortDistanceError);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (DetermineBestLocation.isBetterLocation(location, bestLocation)) {
            bestLocation = location;
            LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());
            points.add(newPoint);
            Log.d("TAG", "New point: " + newPoint.toString());
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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getDistance() {
        return DistanceUtils.getDistance(points);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
