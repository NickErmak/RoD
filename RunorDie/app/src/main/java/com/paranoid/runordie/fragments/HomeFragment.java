package com.paranoid.runordie.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.paranoid.runordie.activities.MainActivity;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.adapters.RVAdapter;
import com.paranoid.runordie.adapters.RecyclerViewCursorAdapter;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.network.NetworkUtils;
import com.paranoid.runordie.utils.BroadcastUtils.ACTION;
import com.paranoid.runordie.utils.SimpleCursorLoader;

import static com.paranoid.runordie.utils.BroadcastUtils.BROADCAST_ACTION;
import static com.paranoid.runordie.utils.BroadcastUtils.EXTRA_ACTION;


public class HomeFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_home_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_HOME";

    private static final int LOADER_ID = 0;

    public static class PostCursorLoader extends SimpleCursorLoader {
        public PostCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DbCrudHelper.getTracks();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ACTION action = (ACTION) intent.getSerializableExtra(EXTRA_ACTION);
            /*switch (action) {
                case SUCCESS_TRACK_UPDATE:
                    mAdapter.setItems(App.getInstance().getState().getHomeTracks());
                    mAdapter.notifyDataSetChanged();
                    break;
                case ERROR_TRACK_UPDATE:
                    Toast.makeText(context, R.string.toast_msg_connection_error, Toast.LENGTH_SHORT).show();
                    break;
                mSwipeRefreshLayout.setRefreshing(false);
            }*/
        }
    };

    private RecyclerViewCursorAdapter<RVAdapter.TrackViewHolder> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public HomeFragment() {
        super(FRAGMENT_TITLE);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshPosts();
            }
        });

        mAdapter = new RVAdapter(null, new RVAdapter.IOnItemClickEvent() {
            @Override
            public void onItemClick(int trackId) {
                //TODO: fix
                //((MainActivity) getActivity()).showFragment(TrackFragment.newInstance(), TrackFragment.FRAGMENT_TAG, false);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.frag_home_rv_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);



        NetworkUtils.getTracks();
       // refreshPosts();
    }

   /* private void refreshPosts() {
        mSwipeRefreshLayout.setRefreshing(true);
        int countPref = PreferenceHelper.getPostCount();
        ApiTwitterProvider.refreshHomeTimeLine(countPref);
    }*/


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




    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new PostCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
