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

import com.paranoid.runordie.adapters.NotificationRecyclerAdapter.IConfigNotification;
import com.paranoid.runordie.utils.DateConverter;

import java.util.Calendar;


public class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_POSITION = "KEY_POSITION";
    private long mExecTime;
    private int mPosition;
    private int mHour, mMinute;

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
        Bundle args = getArguments();
        mExecTime = args.getLong(KEY_TIME);
        mPosition = args.getInt(KEY_POSITION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mExecTime);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, mHour, mMinute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int newHour, int newMinute) {
        if ((newHour != mHour) || (newMinute != mMinute)) {
            long newTime = mExecTime + (60 * (newHour - mHour) + (newMinute - mMinute)) * 60 * 1000;
            IConfigNotification configExecTime = (IConfigNotification) getParentFragment();
            configExecTime.onTimeChanged(mPosition, newTime);
        }
    }
}
