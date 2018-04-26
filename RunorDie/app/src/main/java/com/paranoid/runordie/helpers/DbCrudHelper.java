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
}
