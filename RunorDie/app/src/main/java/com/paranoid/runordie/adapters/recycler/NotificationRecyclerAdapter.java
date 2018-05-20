package com.paranoid.runordie.adapters.recycler;

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
import com.paranoid.runordie.utils.DateConverter;

import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder>{

    public interface IConfigNotification {
        void showTimeDialog(int position, long time);

        void showDateDialog(int position, long date);

        void onTimeChanged(int position, long time);

        void onTitleChanged(int position, String title);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        long execTime;
        String title;
        Button btnExecTime;
        Button btnExecDate;
        EditText etTitle;

        ViewHolder(View v, final IConfigNotification configNotification) {
            super(v);
            etTitle = (EditText) v.findViewById(R.id.list_notification_title);
            etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && getAdapterPosition() != -1) {
                        String newTitle = ((TextView) v).getText().toString();
                        if (!newTitle.equals(title)) {
                            configNotification.onTitleChanged(getAdapterPosition(), newTitle);
                        }
                    }
                }
            });

            btnExecTime = (Button) v.findViewById(R.id.list_notification_time);
            btnExecDate = (Button) v.findViewById(R.id.list_notification_date);
            btnExecTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configNotification.showTimeDialog(getAdapterPosition(), execTime);
                }
            });
            btnExecDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    configNotification.showDateDialog(getAdapterPosition(), execTime);
                }
            });
        }
    }

    private List<Notification> notifications;
    private IConfigNotification configNotification;

    public NotificationRecyclerAdapter(IConfigNotification configNotification, List<Notification> notifications) {
        this.configNotification = configNotification;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_notification,
                parent,
                false
        );
        return new ViewHolder(view, configNotification);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        long execTime = notification.getExecutionTime();
        String title = notification.getTitle();

        holder.execTime = execTime;
        holder.title = title;
        holder.etTitle.setText(title);
        holder.btnExecTime.setText(DateConverter.getTimeString(execTime));
        holder.btnExecDate.setText(DateConverter.getDateString(execTime));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @Override
    public long getItemId(int position) {
        Log.e("TAG", "getItemId");

        return notifications.get(position).getId();
    }
}

