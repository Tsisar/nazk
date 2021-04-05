package ua.com.tsisar.nazk.util;

import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import ua.com.tsisar.nazk.util.date.Year;

public class Date implements Comparable <Date>{
    private static final String DATE_FORMAT = "%1$02d.%2$02d.%3$04d";
    private int year;
    private int month;
    private int day;

    public Date() {
    }

    public Date set(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    public Date set(Date date) {
        this.year = date.getYear();
        this.month = date.getMonth();
        this.day = date.getDay();
        return this;
    }

    public Date now() {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DATE);
        return this;
    }

    public void clear(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public boolean isClear() {
        return year == 0 && month == 0 && day == 0;
    }

    @Override
    public int compareTo(Date date) {
        return (int)(toSeconds() - date.toSeconds());
    }

    public long toSeconds() {
        long res = 0;

        for(int y = 1970; y < year; y++){
            res += Year.forYear(y).getDays();
        }
        for(int m = 0; m < month; m++){
            res += Year.forYear(year).forMonth(m).getDays();
        }
        res += day - 1;

        return TimeUnit.SECONDS.convert(res, TimeUnit.DAYS);
    }

    public long toMillis(){
        return toSeconds()*1000;
    }

    public Long toLong(){
        if(isClear()){
            return null;
        }
        return toSeconds();
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format(DATE_FORMAT, day, month+1, year);
    }
}