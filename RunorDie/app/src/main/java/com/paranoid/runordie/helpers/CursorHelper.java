package com.paranoid.runordie.helpers;

import android.database.Cursor;

import com.paranoid.runordie.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class CursorHelper {

    public static List<Notification> getNotifications(Cursor data) {
        List<Notification> notifications = new ArrayList<>();

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
                    notifications.add(notification);
                } while (data.moveToNext());
            }
        }
        return notifications;
    }
}
