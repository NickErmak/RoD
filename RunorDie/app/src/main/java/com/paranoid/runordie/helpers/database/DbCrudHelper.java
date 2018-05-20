package com.paranoid.runordie.helpers.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import static com.paranoid.runordie.App.getInstance;

public class DbCrudHelper {

    protected static void exec(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    protected static long insert(String sql, String[] columns) {
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
