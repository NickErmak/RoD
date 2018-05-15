package com.paranoid.runordie.utils;

import android.support.design.widget.Snackbar;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.server.NetworkException;

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

    public static void showSnackbar(NetworkException exception) {
        int errorMsgId;
        switch (exception.getErrorCode()) {
            case "INVALID_CREDENTIALS":
                errorMsgId = R.string.invalid_credentials_error;
                break;
            default:
                errorMsgId = R.string.default_error;
                break;
        }
        showSnackbar(errorMsgId);
    }
}
