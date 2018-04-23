package com.paranoid.runordie.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "RoD.db";
    public static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("TAG", "creating database");
        for (int i = 1; i <= DB_VERSION; i++) {
            migrate(db, i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("TAG", "upgrading database");
        while (oldVersion < newVersion) {
            migrate(db, ++oldVersion);
        }
    }

    private void migrate(SQLiteDatabase db, int i) {
        Log.e("TAG", "migrate DB to version " + i);
        switch (i) {
            case 1:
                String[] tables = App.getInstance().getResources().getStringArray(R.array.tables);
                for (String sqlCreate : tables) {
                    DbCrudHelper.exec(db, sqlCreate);
                }
                break;
        }
    }
}
