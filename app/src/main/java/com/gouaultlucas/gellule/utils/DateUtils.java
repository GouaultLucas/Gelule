package com.gouaultlucas.gellule.utils;

import java.util.Calendar;

public class DateUtils {
    public static java.util.Date getCurrentDate() {
        return new java.util.Date();
    }
    public static String dateToSqlDateString(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String currentSqlDate() {
        return dateToSqlDateString(getCurrentDate());
    }
}
