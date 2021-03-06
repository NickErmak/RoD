package com.paranoid.runordie.adapters.recycler;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.utils.DateConverter;
import com.paranoid.runordie.utils.DistanceUtils;

import static com.paranoid.runordie.fragments.HomeFragment.IOnTrackClickEvent;

public class TrackRecyclerCursorAdapter extends RecyclerViewCursorAdapter<TrackRecyclerCursorAdapter.TrackViewHolder> {

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        View view;
        long id;
        TextView mTvTrackStartTime;
        TextView mTvTrackRunTime;
        TextView mTvTrackDistance;

        TrackViewHolder(View view, final IOnTrackClickEvent onTrackClickEvent) {
            super(view);
            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTrackClickEvent.onTrackClick(id);
                }
            });
            mTvTrackStartTime = (TextView) view.findViewById(R.id.list_track_startTime_value);
            mTvTrackRunTime = (TextView) view.findViewById(R.id.list_track_runTime_value);
            mTvTrackDistance = (TextView) view.findViewById(R.id.list_track_distance_value);
        }
    }

    private final IOnTrackClickEvent onTrackClickEvent;

    public TrackRecyclerCursorAdapter(Cursor cursor, IOnTrackClickEvent onTrackClickEvent) {
        super(null);
        this.onTrackClickEvent = onTrackClickEvent;
        swapCursor(cursor);
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_track, parent, false);
        return new TrackViewHolder(view, onTrackClickEvent);
    }

    @Override
    protected void onBindViewHolder(TrackViewHolder holder, Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(Track.DB_ID);
        int startTimeColumnIndex = cursor.getColumnIndex(Track.START_TIME);
        int runTimeColumnIndex = cursor.getColumnIndex(Track.RUN_TIME);
        int distanceColumnIndex = cursor.getColumnIndex(Track.DISTANCE);

        long id = cursor.getLong(idColumnIndex);
        long startTime = cursor.getLong(startTimeColumnIndex);
        int runTime = cursor.getInt(runTimeColumnIndex);
        int distance = cursor.getInt(distanceColumnIndex);

        holder.id = id;
        holder.mTvTrackStartTime.setText(DateConverter.getTimeAndDateString(startTime));
        holder.mTvTrackRunTime.setText(DateConverter.parseUnixTimeToTimerTime(runTime));
        holder.mTvTrackDistance.setText(DistanceUtils.getDistanceFormat(distance));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TrackViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.view.setOnClickListener(null);
    }
}
