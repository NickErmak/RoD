package com.paranoid.runordie.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class JsonConverter {

    private static final Gson gson = new Gson();

    public static List<LatLng> convertJson(String jsonPoints) {
        Type listType = new TypeToken<LinkedList<LatLng>>() {
        }.getType();
        return gson.fromJson(jsonPoints, listType);
    }


}
