package com.paranoid.runordie.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class AbstractFragment extends Fragment {

    private FragmentLifeCircle mFragmentLifeCircle;
    private String title;
    private String tag;

    AbstractFragment(String title, String tag) {
        this.title = title;
        this.tag = tag;
    }

    public interface FragmentLifeCircle {
        void onFragmentStart(String title);
        void startProgress();
    }

    public String getFragTag() {
        return tag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentLifeCircle = (FragmentLifeCircle) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFragmentLifeCircle != null) {
            mFragmentLifeCircle.onFragmentStart(title);
        }
    }
}
