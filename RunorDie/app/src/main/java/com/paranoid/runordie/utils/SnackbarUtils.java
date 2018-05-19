package com.paranoid.runordie.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.server.NetworkException;

public class SnackbarUtils {

    private static final String NETWORK_ERROR = "NETWORK_ERROR";
    private static final String INVALID_CREDENTIALS_ERROR = "INVALID_CREDENTIALS";
    private static final String REQUIRED_FIELDS_ARE_EMPTY_ERROR = "REQUIRED_FIELDS_ARE_EMPTY";
    private static final String INVALID_EMAIL_ERROR = "INVALID_EMAIL";
    private static final String EMAIL_ALREADY_EXISTS_ERROR = "EMAIL_ALREADY_EXISTS";
    private static final String INVALID_TOKEN_ERROR  = "INVALID_TOKEN";

    public static void showSnack(int msgId) {
        showSnack(App.getInstance().getString(msgId));
    }

    public static void showSnack(String msg) {
        Activity currentActivity = App.getInstance().getState().getCurrentActivity();
        if (currentActivity != null) {
            Snackbar.make(
                    App.getInstance().getState().getCurrentActivity().getRootLayout(),
                    msg,
                    Snackbar.LENGTH_LONG
            ).show();
        } else {
            Log.e("TAG", msg);
        }
    }

    public static void showSnack(NetworkException exception) {
        int errorMsgId;
        switch (exception.getErrorCode()) {
            case NETWORK_ERROR:
                errorMsgId = R.string.internet_connection_error;
                break;
            case INVALID_CREDENTIALS_ERROR:
                errorMsgId = R.string.invalid_credentials_error;
                break;
            case REQUIRED_FIELDS_ARE_EMPTY_ERROR:
                errorMsgId = R.string.required_fields_are_empty_error;
                break;
            case INVALID_EMAIL_ERROR:
                errorMsgId = R.string.invalid_email_error;
                break;
            case EMAIL_ALREADY_EXISTS_ERROR:
                errorMsgId = R.string.email_already_exists_error;
                break;
            case INVALID_TOKEN_ERROR:
                errorMsgId = R.string.invalid_token_error;
                break;
            default:
                errorMsgId = R.string.default_error;
                break;
        }
        showSnack(errorMsgId);
    }
}
