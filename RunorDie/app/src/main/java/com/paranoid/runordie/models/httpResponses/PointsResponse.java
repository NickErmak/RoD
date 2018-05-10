package com.paranoid.runordie.models.httpResponses;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;

import java.util.List;

public class PointsResponse extends AbstractResponse {

    private List<LatLng> points;

    public List<LatLng> getPoints() {
        return points;
    }

}
