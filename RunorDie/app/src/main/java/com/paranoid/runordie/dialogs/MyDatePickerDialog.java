package com.paranoid.runordie.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.paranoid.runordie.adapters.NotificationRecyclerAdapter.IConfigNotification;

import java.util.Calendar;


public class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_POSITION = "KEY_POSITION";
    private long mExecTime;
    private int mPosition;
    private int mYear, mMonth, mDay;

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
        Bundle args = getArguments();
        mExecTime = args.getLong(KEY_TIME);
        mPosition = args.getInt(KEY_POSITION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mExecTime);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if ((day != mDay) || (month != mMonth) || (year != mYear)) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mExecTime);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            c.set(year, month, day, hour, minute);
            long newExecTime = c.getTimeInMillis();

            IConfigNotification configExecTime = (IConfigNotification) getParentFragment();
            configExecTime.onTimeChanged(mPosition, newExecTime);
        }
    }
}
