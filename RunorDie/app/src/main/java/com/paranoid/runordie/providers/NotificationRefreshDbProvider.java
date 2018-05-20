package com.paranoid.runordie.providers;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.helpers.database.NotificationCrudHelper;
import com.paranoid.runordie.models.Notification;
import com.paranoid.runordie.utils.broadcastUtils.NotificationBroadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class NotificationRefreshDbProvider {

    public static void refreshNotifications(
            List<Notification> notificationsForRefresh,
            Set<Long> notificationIdListForDeleting) {
        if (!App.getInstance().getState().isNotificationDbRefreshing()) {
            Log.d("TAG", "Refreshing notifications..");
            App.getInstance().getState().setNotificationDbRefreshing(true);
            refreshNotificationsAsync(notificationsForRefresh, notificationIdListForDeleting).getResult();
        }
    }

    private static Task<Void> refreshNotificationsAsync(
            List<Notification> notificationsForRefresh,
            Set<Long> notificationIdListForDeleting) {
        List<Task<Void>> refreshTasks = new ArrayList<>();

        for (Notification notification : notificationsForRefresh) {
            refreshTasks.add(updateNotificationAsync(notification));
        }

        for (long id : notificationIdListForDeleting) {
            refreshTasks.add(deleteNotificationAsync(id));
        }

        return Task.whenAll(refreshTasks).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                if (task.isCompleted()) {
                    Log.d("TAG", "Refreshing notifications: SUCCESS");
                } else if (task.isFaulted()) {
                    Log.e("TAG", "Refreshing notifications: ERROR " + task.getError().getMessage());
                }
                App.getInstance().getState().setNotificationDbRefreshing(false);
                NotificationBroadcast.sendBroadcast(NotificationBroadcast.ACTION.REFRESHING_DB_SUCCESS);
                return null;
            }
        },  Task.UI_THREAD_EXECUTOR);
    }

    private static Task<Void> deleteNotificationAsync(final long id) {
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() {
                NotificationCrudHelper.deleteNotification(id);
                return null;
            }
        });
    }

    private static Task<Void> updateNotificationAsync(final Notification notification) {
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() {
                switch (notification.getCrudStatus()) {
                    case INSERT:
                        NotificationCrudHelper.insertNotification(notification);
                        break;
                    case UPDATE:
                        NotificationCrudHelper.updateNotification(notification);
                        break;
                }
                notification.setCrudStatus(Notification.CRUD_STATUS.NONE);
                return null;
            }
        });
    }
}
