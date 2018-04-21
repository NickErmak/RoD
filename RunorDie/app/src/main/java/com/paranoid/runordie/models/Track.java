package com.paranoid.runordie.models;

import java.util.Date;
import java.util.List;

public class Track {

    private Long id;
    private Date beginsAt;
    private long time;
    private int distance;
    private List<PointD> points;

    public Track(Long id) {
        this.id = id;
    }

    public Track(Long id, Date beginsAt, long time, int distance, List<PointD> points) {
        this.id = id;
        this.beginsAt = beginsAt;
        this.time = time;
        this.distance = distance;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginsAt() {
        return beginsAt;
    }

    public void setBeginsAt(Date beginsAt) {
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

    public List<PointD> getPoints() {
        return points;
    }

    public void setPoints(List<PointD> points) {
        this.points = points;
    }
}
