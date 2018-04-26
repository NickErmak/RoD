package com.paranoid.runordie.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.adapters.NotificationAdapter;
import com.paranoid.runordie.adapters.RecyclerViewCursorAdapter;
import com.paranoid.runordie.adapters.TrackAdapter;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.helpers.SwipeController;
import com.paranoid.runordie.network.NetworkUtils;
import com.paranoid.runordie.utils.SimpleCursorLoader;

public class NotificationFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_track_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_TRACK";
    private static final int LOADER_ID = 1;

    public static class NotificationCursorLoader extends SimpleCursorLoader {
        public NotificationCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DbCrudHelper.loadNotifications();
        }
    }

    private RecyclerViewCursorAdapter<NotificationAdapter.NotificationViewHolder> mAdapter;

    public NotificationFragment() {
        super(FRAGMENT_TITLE, FRAGMENT_TAG);
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DbCrudHelper.loadNotifications();

        if (getActivity() != null) {
            ((FragmentLifeCircle) getActivity()).startProgress();
        }

        mAdapter = new NotificationAdapter(null, (AppCompatActivity) getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.frag_notification_rv_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);



        /*view.findViewById(R.id.frag_home_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RunActivity.class));
            }
        });*/
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new NotificationCursorLoader(getActivity());
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
