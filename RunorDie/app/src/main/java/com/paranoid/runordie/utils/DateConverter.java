package com.paranoid.runordie.utils;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private static final int MILLISECONDS_IN_SECONDS = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOURS = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final char SYMBOL_ZERO = '0';
    private static final char SYMBOL_NEW_LINE = '\n';
    private static final String timerFormat = App.getInstance().getString(R.string.run_timer_format);

    public static String getDateString(long date) {
       return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(date));
    }

    public static String getTimeString(long time) {
        return SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(time));
    }

    public static String getTimeAndDateString(long date) {
        return getTimeString(date) +
                SYMBOL_NEW_LINE +
                getDateString(date);
    }

    public static String getCurrentTimerTime(Long startTime) {
        int millis = (int) (System.currentTimeMillis() - startTime);
        return parseUnixTimeToTimerTime(millis);
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
}
