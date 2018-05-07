package com.paranoid.runordie.models;

import com.paranoid.runordie.activities.BaseActivity;

public class State {
    private Session mActiveSession;
    private BaseActivity mCurrentActivity = null;
    private boolean mIsLoading;

    public Session getActiveSession() {
        return mActiveSession;
    }

    public void setActiveSession(Session activeSession) {
        mActiveSession = activeSession;
    }

    public BaseActivity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(BaseActivity currentActivity) {
        mCurrentActivity = currentActivity;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }
}
