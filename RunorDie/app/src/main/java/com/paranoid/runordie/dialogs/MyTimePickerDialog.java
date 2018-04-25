package com.paranoid.runordie.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public static MyTimePickerDialog newInstance(String permission) {
        return new MyTimePickerDialog();
    }

    public MyTimePickerDialog() {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.e("TAG", "time = " + hourOfDay + ':' + minute);
    }
}
