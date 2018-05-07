package com.paranoid.runordie.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Track {

    public static final String _ID = "_id";
    public static final String ID = "serverId";
    public static final String START_TIME = "startTime";
    public static final String RUN_TIME = "runTime";
    public static final String DISTANCE = "distance";
    public static final String POINTS = "points";

    private long _id;   //DB id
    private long id;    // Server id
    private long startTime;
    private long runTime;
    private int distance;
    private List<LatLng> points;

    public Track(Long id, Long _id) {
        this.id = id;
        this._id = _id;
    }

    public Track(long startTime, long runTime, int distance, List<LatLng> points) {
        this(null, null, startTime, runTime, distance, points);
    }

    public Track(Long id, Long _id, long startTime, long runTime, int distance, List<LatLng> points) {
        this.id = id;
        this._id = _id;
        this.startTime = startTime;
        this.runTime = runTime;
        this.distance = distance;
        this.points = points;
    }

    public Long getDbId() {
        return _id;
    }

    public void setDbId(Long dbId) {
        this._id = dbId;
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

    public long getServerId() {
        return id;
    }

    public void setServerId(long serverId) {
        this.id = serverId;
    }

    @Override
    public String toString() {
        return "Track{" +
                "_id=" + _id +
                ", id=" + id +
                ", startTime=" + startTime +
                ", runTime=" + runTime +
                ", distance=" + distance +
                ", points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        return id == track.id;
    }
}
