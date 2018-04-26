package com.paranoid.runordie.utils;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Iterator;
import java.util.List;

public class DistanceUtils {

    //TODO: fix for points size = 1 or it works:)
    public static int getDistance(List<LatLng> points) {
        Log.e("TAG", "getDistance method. Points number = " + points.size());

        int distance = 0;
        float[] result = new float[1];

        Iterator<LatLng> iterator = points.iterator();

        LatLng endPoint = iterator.next();
        LatLng startPoint;

        while (iterator.hasNext()) {
            startPoint = endPoint;
            endPoint = iterator.next();
            Location.distanceBetween(
                    startPoint.latitude,
                    startPoint.longitude,
                    endPoint.latitude,
                    endPoint.longitude,
                    result
            );
            distance += result[0];
        }
        return distance;
    }
}
