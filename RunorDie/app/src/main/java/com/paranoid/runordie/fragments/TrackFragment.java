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
import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.database.TrackCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.utils.DateConverter;
import com.paranoid.runordie.utils.DistanceUtils;
import com.paranoid.runordie.utils.JsonConverter;
import com.paranoid.runordie.utils.SimpleCursorLoader;

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
            return TrackCrudHelper.getTrack(trackId);
        }
    }

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_track_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_TRACK";
    private static final int LOADER_ID = 3;
    private static final String KEY_TRACK_ID = "KEY_TRACK_ID";

    private int mapPadding;

    private long trackId;
    private List<LatLng> mPoints;
    private SupportMapFragment mMapFragment;
    private TextView mTvRunTime, mTvDistance;

    public TrackFragment() {
        super(
                FRAGMENT_TITLE,
                FRAGMENT_TAG,
                R.id.frag_track_root_layout);
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
        if (getArguments() != null) {
            trackId = getArguments().getLong(KEY_TRACK_ID);
        }
        return inflater.inflate(R.layout.fragment_track, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvDistance = view.findViewById(R.id.frag_track_distance_value);
        mTvRunTime = view.findViewById(R.id.frag_track_runTime_value);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_track_map);
        mapPadding = getResources().getInteger(R.integer.track_frag_map_padding);

        if (App.getInstance().getState().isTrackLoading()) {
            showProgress(true);
        } else {
            loadTrackFromDB();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapFragment = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .title(getString(R.string.frag_track_map_marker_start))
                .position(mPoints.get(0))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
        googleMap.addMarker(new MarkerOptions()
                .title(getString(R.string.frag_track_map_marker_finish))
                .position(mPoints.get(mPoints.size() - 1))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        googleMap.addPolyline(new PolylineOptions()
                .color(getResources().getColor(R.color.app_accent))
                .clickable(true)
                .addAll(mPoints)
        );

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : mPoints) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, mapPadding));

        showProgress(false);
    }

    private void loadTrackFromDB() {
        Log.d("TAG", "loading track from DB..");
        App.getInstance().getState().setTrackLoading(true);
        showProgress(true);
        LoaderManager lm = getLoaderManager();
        if (lm.getLoader(LOADER_ID) == null) {
            lm.initLoader(LOADER_ID, null, this);
        } else {
            lm.restartLoader(LOADER_ID, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new PointsCursorLoader(getContext(), trackId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("TAG", "loading track from DB: SUCCESS");
        App.getInstance().getState().setTrackLoading(false);
        if (data != null) {
            if (data.moveToFirst()) {
                int runTimeColumnIndex = data.getColumnIndex(Track.RUN_TIME);
                int distanceColumnIndex = data.getColumnIndex(Track.DISTANCE);
                int pointsColumnIndex = data.getColumnIndex(Track.POINTS);

                int runTime = data.getInt(runTimeColumnIndex);
                int distance = data.getInt(distanceColumnIndex);
                String pointsJson = data.getString(pointsColumnIndex);

                mPoints = JsonConverter.convertJson(pointsJson);
                mTvRunTime.setText(DateConverter.parseUnixTimeToTimerTime(runTime));
                mTvDistance.setText(DistanceUtils.getDistanceFormat(distance));
            }
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
