package com.paranoid.runordie.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Network;
import android.os.Bundle;
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
import com.paranoid.runordie.adapters.RecyclerViewCursorAdapter;
import com.paranoid.runordie.adapters.TrackAdapter;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.models.Track;
import com.paranoid.runordie.models.httpResponses.TrackResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.server.NetworkProvider;
import com.paranoid.runordie.utils.PreferenceUtils;
import com.paranoid.runordie.utils.SimpleCursorLoader;

import java.util.LinkedList;
import java.util.List;

import static com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast.ACTION;
import static com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast.BROADCAST_ACTION;
import static com.paranoid.runordie.utils.broadcastUtils.HomeBroadcast.EXTRA_ACTION;

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
            return DbCrudHelper.loadTrackCursor();
        }
    }

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_home_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_HOME";
    private static final int LOADER_ID = 0;

    private RecyclerViewCursorAdapter<TrackAdapter.TrackViewHolder> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private IOnTrackClickEvent mOnTrackClickEvent;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "broadcast received (home frag)");
            ACTION action = (ACTION) intent.getSerializableExtra(EXTRA_ACTION);
            switch (action) {
              /*  case ERROR:
                    String errorCode = intent.getStringExtra(EXTRA_ERROR);
                    if (errorCode.equals(AbstractResponse.INVALID_TOKEN)) {
                        //TODO: snack
                        Toast.makeText(getApplicationContext(), "Authorization error", Toast.LENGTH_LONG).show();

                        Intent intent2 = new Intent(getApplicationContext(), AuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        finish();
                    }

                    Log.e("TAG", "error_code: " + errorCode);
                    break;*/


                case TRACKS_REFRESHED:
                    App.getInstance().getState().setServerSyncRunning(false);
                    loadTracksFromDB();
                    break;
            }
        }
    };

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

        setFab((FloatingActionButton) view.findViewById(R.id.frag_home_fab));
        setAdapter((RecyclerView) view.findViewById(R.id.frag_home_rv_tracks));
        setSwipeRefresh((SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh));

        if (!PreferenceUtils.isFirstLaunch()) {
            loadTracksFromDB();
        }
        refreshPosts();
    }

    private void setFab(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RunActivity.class));
            }
        });
    }

    private void setSwipeRefresh(SwipeRefreshLayout swipeRefresh) {
        mSwipeRefreshLayout = swipeRefresh;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.app_accent, R.color.app_primary_dark, R.color.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });
    }

    private void setAdapter(RecyclerView recyclerView) {
        mAdapter = new TrackAdapter(null, mOnTrackClickEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private void loadTracksFromDB() {
        LoaderManager lm = getLoaderManager();
        if (lm.getLoader(LOADER_ID) == null) {
            lm.initLoader(LOADER_ID, null, this);
        } else {
            lm.restartLoader(LOADER_ID, null, this);
        }
    }

    private void refreshPosts() {
        if (!App.getInstance().getState().isServerSyncRunning()) {
            App.getInstance().getState().setServerSyncRunning(true);
            mSwipeRefreshLayout.setRefreshing(true);
            NetworkProvider.syncDbWithServer();
        }
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

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(BROADCAST_ACTION)
        );
    }
}
