package com.paranoid.runordie.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;


import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

import static com.paranoid.runordie.utils.PermissionUtils.MY_PERMISSIONS_REQUEST;

public class PermissionDialog extends DialogFragment {

    public static final String KEY_PERMISSION = "KEY_PERMISSION";
    public static final String KEY_PERMISSION_TITLE = "KEY_PERMISSION_TITLE";

    private String permissionDialogTitle;
    private String permissionDialogMsgFormat;

    private String permission;
    private String permissionName;

    public static PermissionDialog newInstance(String permission, String permissionTitle) {
        PermissionDialog result = new PermissionDialog();
        Bundle args = new Bundle();
        args.putString(KEY_PERMISSION, permission);
        args.putString(KEY_PERMISSION_TITLE, permissionTitle);
        result.setArguments(args);
        return result;
    }

    public PermissionDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = getArguments().getString(KEY_PERMISSION);
        permissionName = getArguments().getString(KEY_PERMISSION_TITLE);

        permissionDialogTitle = App.getInstance().getString(R.string.permission_dialog_title);
        permissionDialogMsgFormat = App.getInstance().getString(R.string.permission_dialog_msg_format);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(permissionDialogTitle)
                .setMessage(
                    String.format(permissionDialogMsgFormat, permissionName)
                )
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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
