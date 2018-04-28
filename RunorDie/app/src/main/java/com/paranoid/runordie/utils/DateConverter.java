package com.paranoid.runordie.utils;


import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static DateFormat dateFormatter;
    private static DateFormat timeFormatter;
    private static String timeFormat;

    static {
        String timePattern = "HH:mm:ss";
        String datePattern = "MM/dd/yyyy";

        dateFormatter = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        timeFormatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

        timeFormat = App.getInstance().getString(R.string.format_runTime);
    }

    public static String parseDateToString(long date) {
        return dateFormatter.format(new Date(date));
    }

    public static String parseTimeToString(long time) {
        return timeFormatter.format(new Date(time));
    }





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
    }

    public static String parseTimeToString(Long time) {
        //TODO: fix time format 02:01:44
        int seconds, minutes, hours;
        seconds = (int) (time / 1000);
        minutes = seconds / 60;
        hours = minutes / 60;

        seconds -= (minutes * 60);
        minutes -= (hours * 60);

        return String.format(timeFormat, hours, minutes, seconds);
    }*/
}
