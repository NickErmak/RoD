package com.paranoid.runordie.utils;


import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateConverter {

    private static final int MILLISECONDS_IN_SECONDS = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOURS = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final char SYMBOL_ZERO = '0';

    private static DateFormat dateFormatter;
    private static DateFormat timeFormatter;
    private static String timeFormat;

    static {

        String datePattern = "MM/dd/yyyy";

        dateFormatter = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        timeFormatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

        timeFormat = App.getInstance().getString(R.string.format_runTime);
    }

    public static String parseDateToString(long date) {
        return dateFormatter.format(new Date(date));
    }

  /*  public static String parseTimeToString(long time) {
        return timeFormatter.format(new Date(time));
    }

*/



   /* public static Date parseStringToDate(String dateString) {

        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String parseDateToString(Long date) {
        return formatter.format(date);
    }*/

    public static String parseTimeToString(Long time) {
        //TODO: fix time format 02:01:44
        String timePattern = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(timePattern, Locale.getDefault());
        return sdf.format(new Date(time));




/*


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int seconds, minutes, hours;
        seconds = calendar.get(Calendar.SECOND);
        minutes = calendar.get(Calendar.MINUTE);
        hours = calendar.get(Calendar.HOUR_OF_DAY);

        return String.format(timeFormat, hours, minutes, seconds);*/
    }

    public static String getTimerTime(Long startTime) {
        long timerTime = System.currentTimeMillis() - startTime;
        String timeFormat = "%1$s:%2$s:%3$s.%4$s";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timerTime);


        long millis = System.currentTimeMillis() - startTime;

            int seconds = (int) (millis / MILLISECONDS_IN_SECONDS);
            int minutes = seconds / SECONDS_IN_MINUTE;
            int hours = minutes / MINUTES_IN_HOURS;
            millis = (millis % MILLISECONDS_IN_SECONDS) / 10; //show two milliseconds digits
            seconds = seconds % SECONDS_IN_MINUTE;
            minutes = minutes % MINUTES_IN_HOURS;
            hours = hours % HOURS_IN_DAY;

        return String.format(
                Locale.getDefault(),
                timeFormat,
                convertTimeDigit(hours),
                convertTimeDigit(minutes),
                convertTimeDigit(seconds),
                convertTimeDigit(millis)
        );
    }

    private static String convertTimeDigit(Number timeValue) {
        String stringValue = String.valueOf(timeValue);
        if (stringValue.length() == 1) {
            stringValue = SYMBOL_ZERO + stringValue;
        }
        return stringValue;
    }
}
