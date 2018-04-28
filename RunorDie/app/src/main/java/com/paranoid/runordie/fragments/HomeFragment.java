package com.paranoid.runordie.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.adapters.RecyclerViewCursorAdapter;
import com.paranoid.runordie.adapters.TrackAdapter;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.network.NetworkUtils;
import com.paranoid.runordie.utils.SimpleCursorLoader;


public class HomeFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface IOnTrackClickEvent {
        void onTrackClick(long trackId);
    }

    public static class TrackCursorLoader extends SimpleCursorLoader {
        TrackCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DbCrudHelper.loadTracks();
        }
    }

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_home_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_HOME";
    private static final int LOADER_ID = 0;

    private RecyclerViewCursorAdapter<TrackAdapter.TrackViewHolder> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private IOnTrackClickEvent mOnTrackClickEvent;


    public HomeFragment() {
        super(FRAGMENT_TITLE, FRAGMENT_TAG);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnTrackClickEvent = (IOnTrackClickEvent) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.frag_home_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RunActivity.class));
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.app_accent, R.color.app_primary_dark, R.color.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

        mAdapter = new TrackAdapter(null, mOnTrackClickEvent);
        RecyclerView recyclerView = view.findViewById(R.id.frag_home_rv_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setRefreshing(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        //TODO: add refresh from server
        NetworkUtils.getTracks();
        // refreshPosts();
    }

    private void refreshPosts() {
        mSwipeRefreshLayout.setRefreshing(true);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new TrackCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        Log.e("TAG", "swap cursor");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
