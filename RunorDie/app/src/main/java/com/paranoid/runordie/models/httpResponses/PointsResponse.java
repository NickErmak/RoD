package com.paranoid.runordie.models.httpResponses;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PointsResponse extends AbstractResponse {

    private List<LatLng> points;

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }
}
