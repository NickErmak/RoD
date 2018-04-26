package com.paranoid.runordie.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;


import com.paranoid.runordie.R;

import static com.paranoid.runordie.utils.PermissionUtils.MY_PERMISSIONS_REQUEST;

public class PermissionDialog extends DialogFragment {

    public static final String KEY_PERMISSION = "KEY_PERMISSION";
    private String permission;

    public static PermissionDialog newInstance(String permission) {
        PermissionDialog result = new PermissionDialog();
        Bundle args = new Bundle();
        args.putString(KEY_PERMISSION, permission);
        result.setArguments(args);
        return result;
    }

    public PermissionDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = getArguments().getString(KEY_PERMISSION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("Permission necessary")
                .setMessage(permission.replace("android.permission.", "") + " permission is necessary")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST);
                    }
                })
                .create();
    }
}
