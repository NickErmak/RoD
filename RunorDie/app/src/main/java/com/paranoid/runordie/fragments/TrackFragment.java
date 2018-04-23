package com.paranoid.runordie.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

public class TrackFragment extends AbstractFragment implements OnMapReadyCallback {

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_track_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_TRACK";

    public TrackFragment() {
        super(FRAGMENT_TITLE);
    }

    public static TrackFragment newInstance() {
        return new TrackFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_track_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO: fix and add auto camera
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .color(getResources().getColor(R.color.app_accent))
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        // Set listeners for click events.
      //  googleMap.setOnPolylineClickListener(this);
      //  googleMap.setOnPolygonClickListener(this);

    }
}
