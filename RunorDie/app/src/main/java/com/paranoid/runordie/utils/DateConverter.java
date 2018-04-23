package com.paranoid.runordie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static SimpleDateFormat formatter;

    static {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        formatter = new SimpleDateFormat(pattern);
    }

    public static Date parseStringToDate(String dateString) {
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
}
