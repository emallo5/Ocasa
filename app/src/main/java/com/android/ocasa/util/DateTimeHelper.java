package com.android.ocasa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ignacio on 04/02/16.
 */
public class DateTimeHelper {

    private static final String TIMEZONE_PATTERN = "Z";
    private static final String SERVER_DATE_PATTERN = "yyyy/MM/dd";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm";

    public static Date serverParseDate(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat(SERVER_DATE_PATTERN, Locale.getDefault());

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static Date parseTime(String time){

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        try {
            return dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String formatTime(Date time){

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        return dateFormat.format(time);
    }

    public static Date parseDate(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String serverFormatDate(Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat(SERVER_DATE_PATTERN, Locale.getDefault());

        return dateFormat.format(date);
    }

    public static String formatDate(Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());

        return dateFormat.format(date);
    }

    public static Date parseDateTime(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN, Locale.getDefault());

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String formatDateTime(Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN, Locale.getDefault());

        return dateFormat.format(date);
    }

    public static String getDeviceTimezone(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat(TIMEZONE_PATTERN, Locale.getDefault());
        return date.format(currentLocalTime);
    }
}
