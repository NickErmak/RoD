package com.paranoid.runordie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Track;

import static com.paranoid.runordie.utils.DateConverter.parseDateToString;

public class RVAdapter extends RecyclerViewCursorAdapter<RVAdapter.TrackViewHolder> {

    public interface IOnItemClickEvent {
        void onItemClick(int trackId);
    }

    private static final String TAG = RVAdapter.class.getSimpleName();
    private final IOnItemClickEvent onItemClickEvent;

    public RVAdapter(Cursor cursor, IOnItemClickEvent onItemClickEvent) {
        super(null);
        this.onItemClickEvent = onItemClickEvent;
        swapCursor(cursor);
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_track, parent, false);
        return new TrackViewHolder(view, onItemClickEvent);
    }

    @Override
    protected void onBindViewHolder(TrackViewHolder holder, Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex("_id");
        int startTimeColumnIndex = cursor.getColumnIndex(Track.START_TIME);
        int runTimeColumnIndex = cursor.getColumnIndex(Track.RUN_TIME);
        int distanceColumnIndex = cursor.getColumnIndex(Track.DISTANCE);

        int id = cursor.getInt(idColumnIndex);
        long startTime = cursor.getLong(startTimeColumnIndex);
        int runTime = cursor.getInt(runTimeColumnIndex);
        int distance = cursor.getInt(distanceColumnIndex);

        holder.id = id;
        holder.mTvTrackStartTime.setText(parseDateToString(startTime));
        holder.mTvTrackRunTime.setText(String.valueOf(runTime));
        holder.mTvTrackDistance.setText(String.valueOf(distance));
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        int id;
        TextView mTvTrackStartTime;
        TextView mTvTrackRunTime;
        TextView mTvTrackDistance;

        TrackViewHolder(View v, final IOnItemClickEvent onItemClickEvent) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickEvent.onItemClick(id);
                }
            });
            mTvTrackStartTime = (TextView) v.findViewById(R.id.list_track_startTime_value);
            mTvTrackRunTime = (TextView) v.findViewById(R.id.list_track_runTime_value);
            mTvTrackDistance = (TextView) v.findViewById(R.id.list_track_distance_value);
        }
    }
}
