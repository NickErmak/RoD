package com.paranoid.runordie.helpers;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Notification;
import com.paranoid.runordie.providers.NotificationRefreshDbProvider;
import com.paranoid.runordie.utils.AlarmManagerUtils;
import com.paranoid.runordie.utils.DateConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationHelper {

    private List<Notification> notifications;
    private Set<Long> idListForDeleting;

    public NotificationHelper() {
        notifications = new ArrayList<>();
        idListForDeleting = new HashSet<>();
    }

    public int addNewNotification() {
        int notificationIndex = notifications.size();
        Notification newNotification = new Notification(
                System.currentTimeMillis(),
                String.format(
                        App.getInstance().getString(R.string.notification_title_format),
                        notificationIndex + 1
                )
        );
        newNotification.setCrudStatus(Notification.CRUD_STATUS.INSERT);
        notifications.add(newNotification);
        return notificationIndex;
    }

    public void refreshTime(int index, long newTime) {
        Notification refreshedNotification = notifications.get(index);
        refreshedNotification.setExecutionTime(newTime);
        refreshCrudStatusUpdate(refreshedNotification);
    }

    public void refreshTitle(int index, String newTitle) {
        Notification refreshedNotification = notifications.get(index);
        refreshedNotification.setTitle(newTitle);
        refreshCrudStatusUpdate(refreshedNotification);
    }

    public void deleteNotification(int index) {
        Long id = notifications.get(index).getId();
        if (id != null) {
            idListForDeleting.add(id);
        }
        notifications.remove(index);
    }

    public void save() {
        NotificationRefreshDbProvider.refreshNotifications(notifications, idListForDeleting);
        refreshAlarmNotification();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications.clear();
        this.notifications.addAll(notifications);
    }

    private void refreshAlarmNotification() {
        Notification nextExecuteNotification = null;
        for (Notification notification : notifications) {
            if (notification.getExecutionTime() > System.currentTimeMillis()) {
                if (nextExecuteNotification == null) {
                    nextExecuteNotification = notification;
                } else {
                    if (nextExecuteNotification.getExecutionTime() > notification.getExecutionTime()) {
                        nextExecuteNotification = notification;
                    }
                }
            }
        }

        if (nextExecuteNotification != null) {
            AlarmManagerUtils.setAlarm(nextExecuteNotification);
            Log.d("TAG", "Next notification will execute at "
                    + DateConverter.getTimeAndDateString(nextExecuteNotification.getExecutionTime()));
        } else {
            AlarmManagerUtils.deleteAlarm();
            Log.d("TAG", "No next notifications");
        }
    }

    private void refreshCrudStatusUpdate(Notification notification) {
        Notification.CRUD_STATUS status = notification.getCrudStatus();
        if (status != Notification.CRUD_STATUS.INSERT) {
            notification.setCrudStatus(Notification.CRUD_STATUS.UPDATE);
        }
    }
}
