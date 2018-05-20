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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.adapters.recycler.NotificationRecyclerAdapter;
import com.paranoid.runordie.controllers.SwipeController;
import com.paranoid.runordie.dialogs.MyDatePickerDialog;
import com.paranoid.runordie.dialogs.MyTimePickerDialog;
import com.paranoid.runordie.helpers.CursorHelper;
import com.paranoid.runordie.helpers.NotificationHelper;
import com.paranoid.runordie.helpers.database.NotificationCrudHelper;
import com.paranoid.runordie.utils.SimpleCursorLoader;
import com.paranoid.runordie.utils.SnackbarUtils;
import com.paranoid.runordie.utils.broadcastUtils.NotificationBroadcast;

public class NotificationFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        NotificationRecyclerAdapter.IConfigNotification, SwipeController.ISwipeEvent {

    public static class NotificationCursorLoader extends SimpleCursorLoader {
        NotificationCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return NotificationCrudHelper.loadNotifications();
        }
    }

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_NOTIFICATION";
    private static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_notification_title);
    private static final String INVALID_DATE_MSG = App.getInstance().getString(R.string.invalid_time_input);
    private static String RECYCLER_STATE_KEY = "RECYCLER_STATE_KEY";
    private static final int LOADER_ID = 1;

    private NotificationHelper notificationHelper;
    private NotificationRecyclerAdapter adapter;

    private RecyclerView recyclerView;
    private Parcelable recyclerState;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationBroadcast.ACTION action = (NotificationBroadcast.ACTION) intent.getSerializableExtra(
                    NotificationBroadcast.EXTRA_ACTION);
            switch (action) {
                case REFRESHING_DB_SUCCESS:
                    Log.d("TAG", "Broadcast (notification fragment): refreshing DB success");
                    loadNotificationsFromDB();
                    break;
            }
        }
    };

    public NotificationFragment() {
        super(
                FRAGMENT_TITLE,
                FRAGMENT_TAG,
                R.id.frag_notification_root_layout);
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

        recyclerView = view.findViewById(R.id.frag_notification_rv_notifications);
        notificationHelper = new NotificationHelper();
        setFab((FloatingActionButton) view.findViewById(R.id.frag_notification_fab));
        setRecycler(recyclerView, savedInstanceState);

        if (App.getInstance().getState().isNotificationDbRefreshing() ||
                App.getInstance().getState().isNotificationsLoading()) {
            showProgress(true);
        } else {
            loadNotificationsFromDB();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(NotificationBroadcast.BROADCAST_ACTION)
        );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable recyclerState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_STATE_KEY, recyclerState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
        notificationHelper.save();
    }

    private void setFab(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNotificationIndex = notificationHelper.addNewNotification();
                adapter.notifyItemInserted(newNotificationIndex);
            }
        });
    }

    private void setRecycler(RecyclerView recyclerView, Bundle savedInstanceState) {
        adapter = new NotificationRecyclerAdapter(
                this,
                notificationHelper.getNotifications()
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        SwipeController swipeController = new SwipeController(this);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE_KEY);
        }
    }

    private void loadNotificationsFromDB() {
        Log.d("TAG", "loading notifications from DB..");
        App.getInstance().getState().setNotificationsLoading(true);
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
        return new NotificationCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("TAG", "loading notifications from DB: SUCCESS");
        App.getInstance().getState().setNotificationsLoading(false);
        notificationHelper.setNotifications(CursorHelper.getNotifications(data));
        adapter.notifyDataSetChanged();
        if (recyclerState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
        }
        showProgress(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void showTimeDialog(int position, long time) {
        DialogFragment timeDialog = MyTimePickerDialog.newInstance(position, time);
        timeDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showDateDialog(int position, long date) {
        DialogFragment dateDialog = MyDatePickerDialog.newInstance(position, date);
        dateDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onTimeChanged(int position, long time) {
        if (time > System.currentTimeMillis()) {
            notificationHelper.refreshTime(position, time);
            adapter.notifyItemChanged(position);
        } else {
            SnackbarUtils.showSnack(INVALID_DATE_MSG);
        }
    }

    @Override
    public void onTitleChanged(int position, String title) {
        notificationHelper.refreshTitle(position, title);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void handleSwipe(int itemPosition) {
        notificationHelper.deleteNotification(itemPosition);
        adapter.notifyItemRemoved(itemPosition);
    }
}
