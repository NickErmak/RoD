package com.paranoid.runordie;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.paranoid.runordie.helpers.DbOpenHelper;
import com.paranoid.runordie.models.State;

public class App extends Application {

    private static final String VACUUM_COMMAND = "VACUUM";

    private static App instance;
    private State state;
    private SQLiteDatabase db;

    public static App getInstance() {
        return instance;
    }
    public State getState() {
        return state;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        state = new State();

        db = new DbOpenHelper(this).getWritableDatabase();
        db.execSQL(VACUUM_COMMAND);
    }
}
