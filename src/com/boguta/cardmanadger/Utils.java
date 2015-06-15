package com.boguta.cardmanadger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    private Utils() throws InstantiationException {
        throw new InstantiationException("utils class");
    }
    
    public static String getReadableDate(String due) {
        Calendar cal = getCalendar(due);            
        SimpleDateFormat format = new SimpleDateFormat(Constants.READABLE_DATE_PATTERN, Locale.getDefault());
        String dueReadable = format.format(cal.getTime());
        return dueReadable;
    }
    
    public static Calendar getCalendar(String curDue) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.TRELLO_DATE_PATTERN, Locale
                .getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        if (curDue != null) {
            try {
                date = format.parse(curDue);
            } catch (ParseException e) {
                date = new Date(System.currentTimeMillis());
            }
        } else {
            date = new Date(System.currentTimeMillis());
        }
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTime(date);
        return calendar;
    }
}
