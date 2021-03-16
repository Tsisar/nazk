package ua.com.tsisar.nazk.util;

import java.util.Calendar;
import java.util.TimeZone;

public class Date implements Comparable<Date>{
    private static final String TAG = "MyLog";

    private static final String SPLIT = "-";
    private static final String DATE_FORMAT = "%1$04d-%2$02d-%3$02d";
    private static final String DATE_FORMAT_REVERSE = "%3$02d.%2$02d.%1$04d";

    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date() {
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public Date setCurrentDate(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        return this;
    }

    public Date setDate(String date){
        if(date != null && date.length() != 0){
            String[] splitArray = date.split(SPLIT);
            this.year = Integer.parseInt(splitArray[0]);
            this.month = Integer.parseInt(splitArray[1])-1;
            this.day = Integer.parseInt(splitArray[2]);
//        }else{
//            setCurrentDate();
        }
        return this;
    }

    public Date setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    public Date setDate(Date d) {
        this.year = d.getYear();
        this.month = d.getMonth();
        this.day = d.getDay();
        return this;
    }

    @Override
    public String toString() {
        if(!isNull()){
            return String.format(DATE_FORMAT, year, month+1, day);
        }
        return "";
    }

    public String toStringReverse() {
        if(!isNull()){
            return String.format(DATE_FORMAT_REVERSE, year, month+1, day);
        }
        return "";
    }

    public boolean isNull(){
        return year == 0 && month == 0 && day == 0;
    }

    @Override
    public int compareTo(Date o) {
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
