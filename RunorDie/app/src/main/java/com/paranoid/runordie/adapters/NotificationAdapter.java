package com.paranoid.runordie.adapters;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.dialogs.MyTimePickerDialog;
import com.paranoid.runordie.models.Notification;

import static com.paranoid.runordie.utils.DateConverter.parseDateToString;

public class NotificationAdapter extends RecyclerViewCursorAdapter<NotificationAdapter.NotificationViewHolder> {

    public AppCompatActivity mActivity;

    public NotificationAdapter(Cursor cursor, AppCompatActivity activity) {
        super(null);
        mActivity = activity;
        swapCursor(cursor);
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notification, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view, mActivity);
    }

    @Override
    protected void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder, Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(Notification._ID);
        int execTimeColumnIndex = cursor.getColumnIndex(Notification.EXEC_TIME);
        int titleColumnIndex = cursor.getColumnIndex(Notification.TITLE);

        int id = cursor.getInt(idColumnIndex);
        long execTime = cursor.getLong(execTimeColumnIndex);
        String title = cursor.getString(titleColumnIndex);

        holder.id = id;
        holder.mTvNotificationExecTime.setText(parseDateToString(execTime));
        holder.mTvNotificationTitle.setText(title);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        int id;
        TextView mTvNotificationExecTime;
        TextView mTvNotificationTitle;

        NotificationViewHolder(View v, final AppCompatActivity activity) {
            super(v);
            mTvNotificationExecTime = (TextView) v.findViewById(R.id.list_notification_time);
            mTvNotificationTitle = (TextView) v.findViewById(R.id.list_notification_title);
            v.findViewById(R.id.list_notification_time).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new MyTimePickerDialog();
                    newFragment.show(activity.getSupportFragmentManager(), "timePicker");
                }
            });
        }
    }
}
