package com.paranoid.runordie;

import android.app.Application;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;

import com.paranoid.runordie.helpers.DbOpenHelper;
import com.paranoid.runordie.models.State;
import com.paranoid.runordie.receivers.ConnectionChangeReceiver;
import com.paranoid.runordie.utils.PreferenceUtils;

public class App extends Application {

    private static final String VACUUM_COMMAND = "VACUUM";

    private static App instance;
    private State mState;
    private SQLiteDatabase mDB;

    public static App getInstance() {
        return instance;
    }

    public State getState() {
        return mState;
    }

    public SQLiteDatabase getDb() {
        return mDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        mState = new State();

        mDB = new DbOpenHelper(this).getWritableDatabase();
        mDB.execSQL(VACUUM_COMMAND);

        registerReceivers();
    }

    private void registerReceivers() {
        registerReceiver(
                new ConnectionChangeReceiver(),
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        );
    }
}
