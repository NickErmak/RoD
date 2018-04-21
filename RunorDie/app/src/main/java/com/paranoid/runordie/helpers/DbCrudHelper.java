package com.paranoid.runordie.helpers;


import android.database.sqlite.SQLiteDatabase;

public class DbCrudHelper {

    public static void exec(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }
}
