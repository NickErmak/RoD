package com.paranoid.runordie.helpers.database;

import android.database.Cursor;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Notification;

public class NotificationCrudHelper {

    public static Cursor loadNotifications() {
        return App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_notifications),
                null
        );
    }

    public static long insertNotification(Notification notification) {
        Log.d("TAG", "insert notification: " + notification);
        return DbCrudHelper.insert(
                App.getInstance().getString(R.string.sql_insert_notification),
                new String[]{
                        String.valueOf(notification.getExecutionTime()),
                        notification.getTitle()
                }
        );
    }

    public static void updateNotification(Notification notification) {
        Log.d("TAG", "update notification: " + notification);
        App.getInstance().getDb().execSQL(
                App.getInstance().getString(R.string.sql_update_notification),
                new String[]{
                        String.valueOf(notification.getExecutionTime()),
                        notification.getTitle(),
                        String.valueOf(notification.getId())
                }
        );
    }

    public static void deleteNotification(long id) {
        Log.d("TAG", "delete notification: id = " + id
        );
        App.getInstance().getDb().execSQL(
                App.getInstance().getString(R.string.sql_delete_notification),
                new String[]{
                        String.valueOf(id)
                }
        );
    }
}
