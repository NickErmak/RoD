package com.paranoid.runordie.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.utils.DateConverter;
import com.paranoid.runordie.utils.JsonConverter;
import com.paranoid.runordie.utils.SimpleCursorLoader;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class TrackFragment extends AbstractFragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static class PointsCursorLoader extends SimpleCursorLoader {
        private long trackId;

        PointsCursorLoader(Context context, long trackId) {
            super(context);
            this.trackId = trackId;
        }

        @Override
        public Cursor loadInBackground() {
            return DbCrudHelper.getTrack(trackId);
        }
    }

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_track_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_TRACK";
    private static final int LOADER_ID = 3;
    private static final String KEY_TRACK_ID = "KEY_TRACK_ID";

    private long trackId;
    private List<LatLng> mPoints;
    private SupportMapFragment mMapFragment;
    private TextView mTvRunTime, mTvDistance;

    public TrackFragment() {
        super(FRAGMENT_TITLE, FRAGMENT_TAG);
    }

    public static TrackFragment newInstance(long trackId) {
        TrackFragment result = new TrackFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_TRACK_ID, trackId);
        result.setArguments(args);
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        trackId = getArguments().getLong(KEY_TRACK_ID);
        return inflater.inflate(R.layout.fragment_track, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvDistance = view.findViewById(R.id.frag_track_distance_value);
        mTvRunTime = view.findViewById(R.id.frag_track_runTime_value);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_track_map);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO: fix and add auto camera


        googleMap.addMarker(new MarkerOptions()
                .title("Start")
                .position(mPoints.get(0))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
        googleMap.addMarker(new MarkerOptions()
                .title("Finish")
                .position(mPoints.get(mPoints.size() - 1))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        googleMap.addPolyline(new PolylineOptions()
                .color(getResources().getColor(R.color.app_accent))
                .clickable(true)
                .addAll(mPoints)
        );

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : mPoints) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        int padding = 50;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        // Set listeners for click events.
        //  googleMap.setOnPolylineClickListener(this);
        //  googleMap.setOnPolygonClickListener(this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new PointsCursorLoader(getActivity(), trackId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            String pointsJson = null;
            if (data.moveToFirst()) {
                //TODO: should I redownload runTime and distance from DB?
                int runTimeColumnIndex = data.getColumnIndex(Track.RUN_TIME);
                int distanceColumnIndex = data.getColumnIndex(Track.DISTANCE);
                int pointsColumnIndex = data.getColumnIndex(Track.POINTS);

                long runTime = data.getLong(runTimeColumnIndex);
                long distance = data.getInt(distanceColumnIndex);
                pointsJson = data.getString(pointsColumnIndex);

                mPoints = JsonConverter.convertJson(pointsJson);
                mTvRunTime.setText(DateConverter.parseTimeToString(runTime));
                mTvDistance.setText(String.valueOf(distance));
            }
            data.close();

            mMapFragment.getMapAsync(this);
        } else {
            //TODO: snack?
            Log.e("TAG", "can't load track points");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
