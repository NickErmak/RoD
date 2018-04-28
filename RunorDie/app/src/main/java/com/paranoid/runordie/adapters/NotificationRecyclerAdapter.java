package com.paranoid.runordie.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Notification;

import java.util.List;

import static com.paranoid.runordie.utils.DateConverter.parseDateToString;
import static com.paranoid.runordie.utils.DateConverter.parseTimeToString;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {

    public interface IConfigNotification {
        void showTimeDialog(int position, long time);

        void showDateDialog(int position, long date);

        void onTimeChanged(int position, long time);

        void onTitleChanged(int position, String title);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        long execTime;
        String title;
        Button mBtnExecTime;
        Button mBtnExecDate;
        EditText mEtTitle;

        ViewHolder(View v, final IConfigNotification configNotification) {
            super(v);
            mEtTitle = (EditText) v.findViewById(R.id.list_notification_title);
            mEtTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String newTitle = ((TextView) v).getText().toString();
                        if (!newTitle.equals(title)) {
                            mConfigNotification.onTitleChanged(getAdapterPosition(), newTitle);
                        }
                    }
                }
            });

            mBtnExecTime = (Button) v.findViewById(R.id.list_notification_time);
            mBtnExecDate = (Button) v.findViewById(R.id.list_notification_date);
            mBtnExecTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configNotification.showTimeDialog(getAdapterPosition(), execTime);
                }
            });
            mBtnExecDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configNotification.showDateDialog(getAdapterPosition(), execTime);
                }
            });
        }
    }

    private List<Notification> mNotifications;
    private IConfigNotification mConfigNotification;

    public NotificationRecyclerAdapter(IConfigNotification configNotification, List<Notification> notifications) {
        mConfigNotification = configNotification;
        mNotifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_notification,
                parent,
                false
        );
        return new ViewHolder(view, mConfigNotification);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);

        long execTime = notification.getExecutionTime();
        String title = notification.getTitle();

        //TODO: remake to id and then find position by id??

        holder.execTime = execTime;
        holder.title = title;

        holder.mEtTitle.setText(title);
        holder.mBtnExecTime.setText(parseTimeToString(execTime));
        holder.mBtnExecDate.setText(parseDateToString(execTime));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

}

