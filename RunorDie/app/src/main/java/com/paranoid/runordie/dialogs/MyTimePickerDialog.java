package com.paranoid.runordie.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.paranoid.runordie.adapters.recycler.NotificationRecyclerAdapter.IConfigNotification;

import java.util.Calendar;


public class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_POSITION = "KEY_POSITION";
    private static final int MILLISECONDS_IN_SECONDS = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOURS = 60;
    private long execTime;
    private int position;
    private int hour, minute;

    public static MyTimePickerDialog newInstance(int position, long time) {
        MyTimePickerDialog result = new MyTimePickerDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putLong(KEY_TIME, time);
        result.setArguments(args);
        return result;
    }

    public MyTimePickerDialog() {
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
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int newHour, int newMinute) {
        if ((newHour != hour) || (newMinute != minute)) {
            long newTime = execTime + (MINUTES_IN_HOURS * (newHour - hour) + (newMinute - minute))
                    * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECONDS;
            IConfigNotification configExecTime = (IConfigNotification) getParentFragment();
            if (configExecTime != null) {
                configExecTime.onTimeChanged(position, newTime);
            } else {
                Log.e("TAG", "MyTimePickerDialog: configExecTime is null");
            }
        }
    }
}
