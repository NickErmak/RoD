package com.paranoid.runordie.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Track {

    public static final String _ID = "_id";
    public static final String START_TIME = "startTime";
    public static final String RUN_TIME = "runTime";
    public static final String DISTANCE = "distance";
    public static final String POINTS = "points";

    private Long id;
    private long startTime;
    private long runTime;
    private int distance;
    private List<LatLng> points;

    public Track(long startTime, long runTime, int distance, List<LatLng> points) {
        this(null, startTime, runTime, distance, points);
    }

    public Track(Long id, long startTime, long runTime, int distance, List<LatLng> points) {
        this.id = id;
        this.startTime = startTime;
        this.runTime = runTime;
        this.distance = distance;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }
}
