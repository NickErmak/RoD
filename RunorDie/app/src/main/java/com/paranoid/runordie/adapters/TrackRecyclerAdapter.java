package com.paranoid.runordie.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Track;

import java.util.List;

import static com.paranoid.runordie.utils.DateConverter.parseDateToString;

public class TrackRecyclerAdapter extends RecyclerView.Adapter<TrackRecyclerAdapter.ViewHolder> {

    public interface ITrackDetailsEvent {
        void openTrackDetails(long id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTrackStartTime;
        TextView mTvTrackTotalTime;
        TextView mTvTrackDistance;

        ViewHolder(View v) {
            super(v);
            mTvTrackStartTime = (TextView) v.findViewById(R.id.list_track_startTime_value);
            mTvTrackTotalTime = (TextView) v.findViewById(R.id.list_track_runTime_value);
            mTvTrackDistance = (TextView) v.findViewById(R.id.list_track_distance_value);
        }
    }

    private List<Track> mTracks;
    private ITrackDetailsEvent trackDetailsEvent;

    public TrackRecyclerAdapter(Activity activity, List<Track> tracks) {
        trackDetailsEvent = (ITrackDetailsEvent) activity;
        mTracks = tracks;
    }

    public void setItems(List<Track> tracks) {
        mTracks = tracks;
    }

    @NonNull
    @Override
    public TrackRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetLayout = inflater.inflate(R.layout.list_track, parent, false);
        return new TrackRecyclerAdapter.ViewHolder(tweetLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = mTracks.get(position);

        holder.mTvTrackDistance.setText(String.valueOf(track.getDistance()));
    //    holder.mTvTrackStartTime.setText(parseDateToString(track.getBeginsAt()));
    //    holder.mTvTrackTotalTime.setText(parseDateToString(track.getTime()));
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }
}

