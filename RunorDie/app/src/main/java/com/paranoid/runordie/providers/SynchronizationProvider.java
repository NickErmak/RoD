package com.paranoid.runordie.providers;

public class SynchronizationProvider {

    public static void synchronize() {
        SyncDatabaseProvider.syncDBWithServer();
        SyncServerProvider.syncServerWithDb();
    }
}
