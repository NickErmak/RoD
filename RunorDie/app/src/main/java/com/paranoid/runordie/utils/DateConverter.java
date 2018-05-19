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

    private static final String timerFormat;

    private static DateFormat dateFormatter;
    private static DateFormat timeFormatter;
    private static String timeFormat;

    static {
        timerFormat = App.getInstance().getString(R.string.run_timer_format);

        dateFormatter = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        timeFormatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

        timeFormat = App.getInstance().getString(R.string.format_runTime);
    }

    public static String getCurrentTimerTime(Long startTime) {
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
                timerFormat,
                convertTimeDigit(hours),
                convertTimeDigit(minutes),
                convertTimeDigit(seconds),
                convertTimeDigit(millis)
        );
    }

    public static String parseUnixTimeToTimerTime(int time) {
        int millis = time;
        int seconds = (millis / MILLISECONDS_IN_SECONDS);
        int minutes = seconds / SECONDS_IN_MINUTE;
        int hours = minutes / MINUTES_IN_HOURS;
        millis = (millis % MILLISECONDS_IN_SECONDS) / 10; //show two milliseconds digits
        seconds = seconds % SECONDS_IN_MINUTE;
        minutes = minutes % MINUTES_IN_HOURS;
        hours = hours % HOURS_IN_DAY;
        return String.format(
                Locale.getDefault(),
                timerFormat,
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

    public static String parseDateToString(long date) {
        return dateFormatter.format(new Date(date));
    }

    public static String parseTimeToString(long time) {
        return timeFormatter.format(new Date(time));
    }

    public static String parseDateToStringFull(long date) {
        return parseDateToString(date) + " " + parseTimeToString(date);
    }
}
