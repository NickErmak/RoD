package com.paranoid.runordie.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.paranoid.runordie.api.activities.IActionBarHandler;
import com.paranoid.runordie.api.activities.IProgressHandler;

public abstract class AbstractFragment extends Fragment {

    public interface IActivityManager extends IActionBarHandler, IProgressHandler {}

    private IActivityManager mActivityManager;
    private String mTitle;
    private String mTag;

    AbstractFragment(String title, String tag) {
        mTitle = title;
        mTag = tag;
    }

    public String getFragTag() {
        return mTag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityManager = (IActivityManager) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mActivityManager != null) {
            mActivityManager.setActionBarTitle(mTitle);
        }
    }
}
