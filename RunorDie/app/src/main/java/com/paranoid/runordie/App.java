package com.paranoid.runordie;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;

import com.paranoid.runordie.helpers.DbOpenHelper;
import com.paranoid.runordie.models.State;
import com.paranoid.runordie.receivers.ConnectionChangeReceiver;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {

    private static final String VACUUM_COMMAND = "VACUUM";
    private static App instance;

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    private State state;
    private SQLiteDatabase db;

    private RefWatcher refWatcher;

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

        registerReceivers();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    private void registerReceivers() {
        registerReceiver(
                new ConnectionChangeReceiver(),
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        );
    }
}
