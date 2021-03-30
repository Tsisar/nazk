package ua.com.tsisar.nazk.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateEx implements Comparable<DateEx>{
    private static final String FORMAT = "%1$02d.%2$02d.%3$04d";
    private static final String SPLIT = ".";
    private int year = 1970;
    private int month = 0;
    private int day = 1;

    public DateEx() {
    }

    public DateEx set(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    public DateEx set(long seconds){
        long days = TimeUnit.DAYS.convert(seconds, TimeUnit.SECONDS);
        clear();

        while (days >= YEAR.forYear(year).getDays()){
            days -= YEAR.forYear(year).getDays();
            year++;
        }
        while (days >= YEAR.forYear(year).forMonth(month).getDays()){
            days -= YEAR.forYear(year).forMonth(month).getDays();
            month++;
        }
        day += days;
        return this;
    }

    public DateEx set(String date) {
        if(date != null && date.length() != 0) {
            String[] splitArray = date.split(SPLIT);
            this.year = Integer.parseInt(splitArray[0]);
            this.month = Integer.parseInt(splitArray[1]) - 1;
            this.day = Integer.parseInt(splitArray[2]);
        }
        return this;
    }

    public DateEx now() {
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        return this;
    }

    public DateEx clear(){
        year = 1970;
        month = 0;
        day = 1;
        return this;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Long toLong() {
        long res = 0;
        for(int y = 1970; y < year; y++){
            res += YEAR.forYear(y).getDays();
        }
        for(int m = 0; m < month; m++){
            res += YEAR.forYear(year).forMonth(m).getDays();
        }
        res += day - 1;

        return TimeUnit.SECONDS.convert(res, TimeUnit.DAYS);
    }

    @Override
    public String toString() {
        return String.format(FORMAT, day, month+1, year);
    }

    public boolean isClear(){
        return year == 1970 && month == 0 && day == 1;
    }

    @Override
    public int compareTo(DateEx o) {
        if(year > o.getYear()){
            return 1;
        }else if (year < o.getYear()){
            return -1;
        }else if(month > o.getMonth()){
            return 1;
        }else if (month < o.getMonth()){
            return -1;
        }else if(day > o.getDay()){
            return 1;
        }else if(day < o.getDay()){
            return -1;
        }
        return 0;
    }
}
