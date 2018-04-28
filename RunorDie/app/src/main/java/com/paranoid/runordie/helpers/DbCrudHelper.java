package com.paranoid.runordie.helpers;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.models.Notification;
import com.paranoid.runordie.models.Track;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.paranoid.runordie.App.getInstance;

public class DbCrudHelper {

    public static void exec(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    public static Cursor loadTracks() {
        return getInstance().getDb().rawQuery(
                getInstance().getString(R.string.sql_select_tracks),
                null
        );
    }

    public static Cursor loadNotifications() {
        return getInstance().getDb().rawQuery(
                getInstance().getString(R.string.sql_select_notifications),
                null
        );
    }

    public static List<Notification> getNotifications() {
        List<Notification> notifications = new ArrayList<>();

        Cursor cursor = App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_notifications),
                null
        );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow("_id");
                int execTimeIndex = cursor.getColumnIndexOrThrow("execTime");
                int titleIndex = cursor.getColumnIndexOrThrow("title");

                do {
                    Notification notification = new Notification(
                            cursor.getLong(idIndex),
                            cursor.getLong(execTimeIndex),
                            cursor.getString(titleIndex)
                    );
                    notifications.add(notification);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return notifications;
    }

    //TODO: why I convert type from long to String and then paste in DB again int
    public static Cursor getTrack(long trackId) {
        return App.getInstance().getDb().rawQuery(
                App.getInstance().getString(R.string.sql_select_track),
                new String[]{String.valueOf(trackId)}
        );
    }

    public static Long insertTrack(Track track) {
        Gson gson = new GsonBuilder().create();

        String[] args = {
                String.valueOf(track.getStartTime()),
                String.valueOf(track.getDistance()),
                String.valueOf(track.getRunTime()),
                gson.toJson(track.getPoints())
        };

        return insert(
                App.getInstance().getString(R.string.sql_insert_track),
                args
        );
    }

    public static long insert(String sql, String[] columns) {
        SQLiteDatabase db = getInstance().getDb();
        long idInsert;
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindAllArgsAsStrings(columns);
        try {
            idInsert = statement.executeInsert();
        } finally {
            statement.close();
        }
        return idInsert;
    }

    public static long insertNotification(Notification notification) {
        return insert(
                App.getInstance().getString(R.string.sql_insert_notification),
                new String[]{
                        String.valueOf(notification.getExecutionTime()),
                        notification.getTitle()
                }
        );
    }

    public static void updateNotification(Notification notification) {
            App.getInstance().getDb().execSQL(
                    App.getInstance().getString(R.string.sql_update_notification),
                    new String[]{
                            String.valueOf(notification.getExecutionTime()),
                            notification.getTitle(),
                            String.valueOf(notification.getId())
                    }
            );
    }
}
