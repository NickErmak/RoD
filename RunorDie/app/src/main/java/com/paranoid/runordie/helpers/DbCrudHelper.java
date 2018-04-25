package com.paranoid.runordie.helpers;


import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

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
