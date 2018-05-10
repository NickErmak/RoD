package com.paranoid.runordie.adapters;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class LatLngTypeAdapter extends TypeAdapter<LatLng> {

    private static final String LNG = "lng";
    private static final String LAT = "lat";

    //TODO delete
    @Override
    public void write(JsonWriter out, LatLng value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.beginObject();
            out.name(LNG);
            out.value(value.longitude);
            out.name(LAT);
            out.value(value.latitude);
            out.endObject();
        }
    }

    @Override
    public LatLng read(JsonReader in) throws IOException {
        if (in != null) {
            double lat = 0, lng = 0;

            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if (name.equals(LAT)) {
                    lat = in.nextDouble();
                }
                if (name.equals(LNG)) {
                    lng = in.nextDouble();
                }
            }
            in.endObject();

            if (lat != 0 && lng != 0) {
                return new LatLng(lat, lng);
            }
        }

        return null;
    }
}
