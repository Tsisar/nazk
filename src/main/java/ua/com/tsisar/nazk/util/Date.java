package ua.com.tsisar.nazk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Date implements Comparable<Date>{
    //private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT = "dd.MM.yyyy - hh:mm";

    private Calendar calendar;

    public Date() {
        calendar = Calendar.getInstance();
        calendar.clear();
    }

    private Calendar getCalendar(){
        return calendar;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public Date now(){
        calendar = Calendar.getInstance();
        return this;
    }

    public Date set(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        try {
            calendar.clear();
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this;
    }

    public Date set(int year, int month, int day) {
        calendar.clear();
        calendar.set(year, month, day);
        return this;
    }

    public Date set(Date d) {
        calendar.clear();
        calendar.set(d.getYear(), d.getMonth(), d.getDay());
        return this;
    }

    public Date set(long time) {
        java.util.Date d = new java.util.Date();
        d.setTime(TimeUnit.SECONDS.toMillis(time));
        calendar.clear();
        calendar.setTime(d);
        return this;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public Date clear(){
        calendar.clear();
        return this;
    }

    public boolean isClear(){
        return getYear() == 1970 && getMonth() == 0 && getDay() == 1;
    }

    @Override
    public int compareTo(Date d) {
        return calendar.compareTo(d.getCalendar());
    }

    public long toLong(){
        long duration = calendar.getTimeInMillis() + calendar.get(Calendar.ZONE_OFFSET);
        return TimeUnit.MILLISECONDS.toSeconds(duration);
    }
}