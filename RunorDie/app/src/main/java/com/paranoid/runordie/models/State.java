package com.paranoid.runordie.models;

import com.paranoid.runordie.activities.BaseActivity;

public class State {
    private Session activeSession;
    private BaseActivity currentActivity = null;

    private boolean homeTracksLoading;
    private boolean serverSyncRunning;
    private boolean notificationsLoading;
    private boolean notificationDbRefreshing;
    private boolean trackLoading;
    private boolean trackDbRefreshing;
    private boolean trackSaving;

    public Session getActiveSession() {
        return activeSession;
    }

    public void setActiveSession(Session activeSession) {
        this.activeSession = activeSession;
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public boolean isHomeTracksLoading() {
        return homeTracksLoading;
    }

    public void setHomeTracksLoading(boolean homeTracksLoading) {
        this.homeTracksLoading = homeTracksLoading;
    }

    public boolean isServerSyncRunning() {
        return serverSyncRunning;
    }

    public void setServerSyncRunning(boolean serverSyncRunning) {
        this.serverSyncRunning = serverSyncRunning;
    }

    public boolean isNotificationsLoading() {
        return notificationsLoading;
    }

    public void setNotificationsLoading(boolean notificationsLoading) {
        this.notificationsLoading = notificationsLoading;
    }

    public boolean isNotificationDbRefreshing() {
        return notificationDbRefreshing;
    }

    public void setNotificationDbRefreshing(boolean notificationDbRefreshing) {
        this.notificationDbRefreshing = notificationDbRefreshing;
    }

    public boolean isTrackSaving() {
        return trackSaving;
    }

    public void setTrackSaving(boolean trackSaving) {
        this.trackSaving = trackSaving;
    }

    public boolean isTrackDbRefreshing() {
        return trackDbRefreshing;
    }

    public void setTrackDbRefreshing(boolean trackDbRefreshing) {
        this.trackDbRefreshing = trackDbRefreshing;
    }

    public boolean isTrackLoading() {
        return trackLoading;
    }

    public void setTrackLoading(boolean trackLoading) {
        this.trackLoading = trackLoading;
    }
}
