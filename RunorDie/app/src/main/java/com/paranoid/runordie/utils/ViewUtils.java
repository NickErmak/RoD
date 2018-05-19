package com.paranoid.runordie.utils;

import android.view.View;

public class ViewUtils {

    public static void switchVisibility(boolean visible, View...views) {
        for (View view: views) {
            if (visible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }
}
