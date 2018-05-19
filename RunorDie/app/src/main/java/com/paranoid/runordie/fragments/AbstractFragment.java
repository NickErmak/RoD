package com.paranoid.runordie.fragments;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.api.activities.IActionBarHandler;
import com.paranoid.runordie.api.activities.IProgressHandler;
import com.squareup.leakcanary.RefWatcher;

public abstract class AbstractFragment extends Fragment {

    public interface IActivityManager extends IActionBarHandler, IProgressHandler {}

    private IActivityManager activityManager;
    private String title;
    private String tag;
    private int rootLayoutId;

    AbstractFragment(String title, String tag, int rootLayoutId) {
        this.title = title;
        this.tag = tag;
        this.rootLayoutId = rootLayoutId;
    }

    public String getFragTag() {
        return tag;
    }

    public int getRootLayoutId() {
        return rootLayoutId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityManager = (IActivityManager) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (activityManager != null) {
            activityManager.setActionBarTitle(title);
        }
    }

    protected void showProgress(boolean isLoading) {
        if (activityManager != null) {
            activityManager.showProgress(isLoading);
        } else {
            Log.e("TAG", "activity manager is null. frag tag = " + tag);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityManager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
