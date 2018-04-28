package com.paranoid.runordie.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.Test;
import com.paranoid.runordie.activities.RunActivity;
import com.paranoid.runordie.adapters.NotificationRecyclerAdapter;
import com.paranoid.runordie.dialogs.MyDatePickerDialog;
import com.paranoid.runordie.dialogs.MyTimePickerDialog;
import com.paranoid.runordie.helpers.DbCrudHelper;
import com.paranoid.runordie.helpers.SwipeController;
import com.paranoid.runordie.models.Notification;
import com.paranoid.runordie.utils.DateConverter;
import com.paranoid.runordie.utils.SimpleCursorLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class NotificationFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        NotificationRecyclerAdapter.IConfigNotification {

    public static class NotificationCursorLoader extends SimpleCursorLoader {
        NotificationCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DbCrudHelper.loadNotifications();
        }
    }

    public static final String FRAGMENT_TITLE = App.getInstance().getString(R.string.frag_notification_title);
    public static final String FRAGMENT_TAG = "FRAGMENT_TAG_TRACK";
    private static final int LOADER_ID = 1;

    private NotificationRecyclerAdapter mAdapter;
    private IActivityManager mActivityManager;
    private List<Notification> mNotifications;
    private Set<Integer> mPositionListForRefresh;

    public NotificationFragment() {
        super(FRAGMENT_TITLE, FRAGMENT_TAG);
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityManager = (IActivityManager) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.frag_notification_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification newNotification = new Notification(
                        Calendar.getInstance().getTimeInMillis(),
                        "New notification"
                );
                mNotifications.add(newNotification);
                int position = mNotifications.size() - 1;
                mPositionListForRefresh.add(position);
                mAdapter.notifyItemInserted(position);
            }
        });

        if (mActivityManager != null) {
            mActivityManager.showProgress(true);
        }

        mPositionListForRefresh = new LinkedHashSet<>();
        mNotifications = new ArrayList<>();
        mAdapter = new NotificationRecyclerAdapter(this, mNotifications);
        RecyclerView recyclerView = view.findViewById(R.id.frag_notification_rv_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new NotificationCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (mActivityManager != null) {
            mActivityManager.showProgress(false);
        }
        if (data != null) {
            if (data.moveToFirst()) {
                int idIndex = data.getColumnIndexOrThrow(Notification._ID);
                int execTimeIndex = data.getColumnIndexOrThrow(Notification.EXEC_TIME);
                int titleIndex = data.getColumnIndexOrThrow(Notification.TITLE);

                do {
                    Notification notification = new Notification(
                            data.getLong(idIndex),
                            data.getLong(execTimeIndex),
                            data.getString(titleIndex)
                    );
                    mNotifications.add(notification);
                } while (data.moveToNext());
            }
            data.close();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNotifications.clear();
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
        Notification refreshedNotification = mNotifications.get(position);
        refreshedNotification.setExecutionTime(time);
        mPositionListForRefresh.add(position);

        mAdapter.notifyItemChanged(position);

        Log.e("TAG", "position = " + position);
        Log.e("TAG", "time = " + DateConverter.parseDateToString(time));
    }

    @Override
    public void onTitleChanged(int position, String title) {
        Notification refreshedNotification = mNotifications.get(position);
        refreshedNotification.setTitle(title);
        mPositionListForRefresh.add(position);

        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveChanges();
    }

    private void saveChanges() {
        for (int pos: mPositionListForRefresh) {
            Log.e("TAG", "positions:" + mPositionListForRefresh.toString());
            Notification notification = mNotifications.get(pos);
            if (notification.getId() != null) {
                updateNotification(notification);
            } else {
                createNotification(notification);
            }

        }
    }

    private void updateNotification(Notification notification) {
        Log.d("TAG", "update notification. Title = " + notification.getTitle());
        DbCrudHelper.updateNotification(notification);
    }

    private void createNotification(Notification notification) {
        Log.d("TAG", "insert notification. Title = " + notification.getTitle());
        DbCrudHelper.insertNotification(notification);
        Test.createAlarmNotification(notification.getExecutionTime(), notification.getTitle());
    }
}
