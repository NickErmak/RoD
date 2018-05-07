package com.paranoid.runordie.utils;

import android.support.design.widget.Snackbar;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

public class SnackbarUtils {

    public static void showSnackbar(int msgId) {
        showSnackbar(App.getInstance().getString(msgId));
    }

    public static void showSnackbar(String msg) {
        Snackbar.make(
                App.getInstance().getState().getCurrentActivity().getRootLayout(),
                msg,
                Snackbar.LENGTH_LONG
        ).show();
    }
}
