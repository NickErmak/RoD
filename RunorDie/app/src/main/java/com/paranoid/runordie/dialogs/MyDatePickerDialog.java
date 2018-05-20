package com.paranoid.runordie.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.paranoid.runordie.adapters.recycler.NotificationRecyclerAdapter.IConfigNotification;

import java.util.Calendar;


public class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_POSITION = "KEY_POSITION";
    private long execTime;
    private int position;
    private int year, month, day;

    public static MyDatePickerDialog newInstance(int position, long time) {
        MyDatePickerDialog result = new MyDatePickerDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putLong(KEY_TIME, time);
        result.setArguments(args);
        return result;
    }

    public MyDatePickerDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            execTime = getArguments().getLong(KEY_TIME);
            position = getArguments().getInt(KEY_POSITION);
        } else {
            Log.e("TAG", "MyDatePickerDialog: getArguments() is null");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(execTime);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if ((this.day != day) || (this.month != month) || (this.year != year)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(execTime);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            calendar.set(year, month, day, hour, minute);
            long newExecTime = calendar.getTimeInMillis();

            IConfigNotification configExecTime = (IConfigNotification) getParentFragment();
            if (configExecTime != null) {
                configExecTime.onTimeChanged(position, newExecTime);
            } else {
                Log.e("TAG", "MyDatePickerDialog: configExecTime is null");
            }
        }
    }
}
