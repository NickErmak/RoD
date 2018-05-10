package com.paranoid.runordie.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    //don't use simple types because of retrofit requests:
    // in this case null values won't be send to server

    private Long _id;   //DB id
    private Long id;    // Server id
    private Long beginsAt;
    private Long time;
    private Integer distance;
    private List<LatLng> points;

    public Track(Long id) {
        this.id = id;
    }

    public Track(long beginsAt, long time, int distance, List<LatLng> points) {
        this (null, beginsAt, time, distance, points);
    }

    public Track(Long _id, long beginsAt, long time, int distance, List<LatLng> points) {
        this._id = _id;
        this.beginsAt = beginsAt;
        this.time = time;
        this.distance = distance;
        this.points = points;
    }

    public Track(Long id, Long _id, long beginsAt, long time, int distance, List<LatLng> points) {
        this.id = id;
        this._id = _id;
        this.beginsAt = beginsAt;
        this.time = time;
        this.distance = distance;
        this.points = points;
    }

    public Long getDbId() {
        return _id;
    }

    public void setDbId(Long dbId) {
        this._id = dbId;
    }

    public long getBeginsAt() {
        return beginsAt;
    }

    public void setBeginsAt(long beginsAt) {
        this.beginsAt = beginsAt;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
                ", beginsAt=" + beginsAt +
                ", time=" + time +
                ", distance=" + distance +
                ", points=" + points +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        return id.equals(track.id);
    }
}
