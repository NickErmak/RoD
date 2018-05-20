package com.paranoid.runordie.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
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
import com.paranoid.runordie.adapters.recycler.RecyclerViewCursorAdapter;
import com.paranoid.runordie.adapters.recycler.TrackRecyclerCursorAdapter;
import com.paranoid.runordie.helpers.PreferenceHelper;
import com.paranoid.runordie.helpers.database.TrackCrudHelper;
import com.paranoid.runordie.providers.SynchronizationProvider;
import com.paranoid.runordie.utils.SimpleCursorLoader;
import com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast;

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
            return TrackCrudHelper.loadTrackCursor();
        }
    }

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_HOME";
    private static final String FRAGMENT_TTILE = App.getInstance().getString(R.string.frag_home_title);
    private static String RECYCLER_STATE_KEY = "RECYCLER_STATE_KEY";
    private static final int LOADER_ID = 0;

    private RecyclerViewCursorAdapter<TrackRecyclerCursorAdapter.TrackViewHolder> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Parcelable recyclerState;
    private IOnTrackClickEvent onTrackClickEvent;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HomeBroadcast.ACTION action = (HomeBroadcast.ACTION) intent.getSerializableExtra(HomeBroadcast.EXTRA_ACTION);
            switch (action) {
                case SYNCHRONIZATION_SUCCESS:
                    Log.d("TAG", "Broadcast (home fragment): synchronization success");
                    if (PreferenceHelper.isFirstLaunch()) {
                        PreferenceHelper.executeFirstLaunch();
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    loadTracksFromDB();
                    break;
            }
        }
    };

    public HomeFragment() {
        super(
                FRAGMENT_TTILE,
                FRAGMENT_TAG,
                R.id.frag_home_root_layout
        );
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTrackClickEvent = (IOnTrackClickEvent) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.frag_home_rv_tracks);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
        adapter = new TrackRecyclerCursorAdapter(null, onTrackClickEvent);

        setFab((FloatingActionButton) view.findViewById(R.id.frag_home_fab));
        setRecycler(recyclerView, savedInstanceState);
        setSwipeRefresh(swipeRefreshLayout);

        if (PreferenceHelper.isFirstLaunch()) {
            loadTracksFromDB();
        }

        if (App.getInstance().getState().isServerSyncRunning() ||
                App.getInstance().getState().isHomeTracksLoading()) {
            showProgress(true);
        } else {
            refreshPosts();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(HomeBroadcast.BROADCAST_ACTION)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onTrackClickEvent = null;
        adapter = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable recyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_STATE_KEY, recyclerState);
        super.onSaveInstanceState(outState);
    }

    private void setFab(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RunActivity.class));
            }
        });
    }

    private void setSwipeRefresh(final SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.setColorSchemeResources(R.color.app_accent, R.color.app_primary_dark, R.color.refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                App.getInstance().getState().setTrackSynchronized(false);
                refreshPosts();
            }
        });
    }

    private void setRecycler(RecyclerView recyclerView, Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE_KEY);
        }
    }

    private void loadTracksFromDB() {
        Log.d("TAG", "loading tracks from DB..");
        App.getInstance().getState().setHomeTracksLoading(true);
        showProgress(true);
        LoaderManager lm = getLoaderManager();
        if (lm.getLoader(LOADER_ID) == null) {
            lm.initLoader(LOADER_ID, null, this);
        } else {
            lm.restartLoader(LOADER_ID, null, this);
        }
    }

    private void refreshPosts() {
        if (!App.getInstance().getState().isServerSyncRunning()) {
            SynchronizationProvider.synchronize();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new TrackCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("TAG", "loading tracks from DB: SUCCESS");
        App.getInstance().getState().setHomeTracksLoading(false);
        adapter.swapCursor(data);
        if (recyclerState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
        }
        showProgress(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
