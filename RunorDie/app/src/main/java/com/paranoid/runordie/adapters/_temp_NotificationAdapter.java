package com.paranoid.runordie.adapters;


import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Notification;

import static com.paranoid.runordie.utils.DateConverter.parseDateToString;
import static com.paranoid.runordie.utils.DateConverter.parseTimeToString;

public class _temp_NotificationAdapter extends RecyclerViewCursorAdapter<_temp_NotificationAdapter.NotificationViewHolder> {

    public interface IConfigExecTime {
        void showTimeDialog(long id, long time);
        void showDateDialog(long id, long date);
        void onTimeChanged(long id, long time);
        void onDateChanged(long id, long date);
    }

    private IConfigExecTime mConfigExecTime;

    public _temp_NotificationAdapter(Cursor cursor, IConfigExecTime configExecTime) {
        super(null);
        mConfigExecTime = configExecTime;
        swapCursor(cursor);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_notification,
                parent,
                false
        );
        return new NotificationViewHolder(view, mConfigExecTime);
    }

    @Override
    protected void onBindViewHolder(NotificationViewHolder holder, Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(Notification._ID);
        int execTimeColumnIndex = cursor.getColumnIndex(Notification.EXEC_TIME);
        int titleColumnIndex = cursor.getColumnIndex(Notification.TITLE);

        long id = cursor.getLong(idColumnIndex);
        long execTime = cursor.getLong(execTimeColumnIndex);
        String title = cursor.getString(titleColumnIndex);

        holder.id = id;
        holder.execTime = execTime;

        holder.mTvTitle.setText(title);
        holder.mBtnExecTime.setText(parseTimeToString(execTime));
        holder.mBtnExecDate.setText(parseDateToString(execTime));
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        long id;
        long execTime;
        Button mBtnExecTime;
        Button mBtnExecDate;
        TextView mTvTitle;

        NotificationViewHolder(View v, final IConfigExecTime configExecTime) {
            super(v);
            mTvTitle = (TextView) v.findViewById(R.id.list_notification_title);
            mBtnExecTime = (Button) v.findViewById(R.id.list_notification_time);
            mBtnExecDate = (Button) v.findViewById(R.id.list_notification_date);
            mBtnExecTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configExecTime.showTimeDialog(id, execTime);
                }
            });
            mBtnExecDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configExecTime.showDateDialog(id, execTime);
                }
            });
        }
    }
}
