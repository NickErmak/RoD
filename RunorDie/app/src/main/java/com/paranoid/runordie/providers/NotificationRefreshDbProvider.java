package com.paranoid.runordie.providers;

import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.helpers.DbCrudHelper;
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

        return Task.whenAll(refreshTasks).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                NotificationBroadcast.sendBroadcast(NotificationBroadcast.ACTION.REFRESHING_DB_SUCCESS);
                return null;
            }
        });
    }

    private static Task<Void> deleteNotificationAsync(final long id) {
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() {
                DbCrudHelper.deleteNotification(id);
                Log.e("TAG", "delete note");
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
                        DbCrudHelper.insertNotification(notification);
                        Log.e("TAG", "insert note");
                        break;
                    case UPDATE:
                        DbCrudHelper.updateNotification(notification);
                        Log.e("TAG", "update note");
                        break;
                }
                notification.setCrudStatus(Notification.CRUD_STATUS.NONE);
                return null;
            }
        });
    }
}
