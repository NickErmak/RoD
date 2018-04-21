package com.paranoid.runordie.models.httpResponses;

import com.paranoid.runordie.models.PointD;

import java.util.List;

public class PointsResponse extends AbstractResponse {

    private List<PointD> points;

    public List<PointD> getPoints() {
        return points;
    }
    public void setPoints(List<PointD> points) {
        this.points = points;
    }
}
