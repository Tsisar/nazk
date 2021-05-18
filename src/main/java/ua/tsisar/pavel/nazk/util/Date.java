package ua.tsisar.pavel.nazk.util;

import androidx.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Date {
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private LocalDate localDate;

    public Date() {
        localDate = new LocalDate(1970, 1, 1);
    }

    public Date set(int year, int month, int day) {
        localDate = new LocalDate(year, month + 1, day);
        return this;
    }

    public Date set(Date date) {
        localDate = new LocalDate(date.getYear(), date.getMonth() + 1, date.getDay());
        return this;
    }

    public Date now() {
        localDate = LocalDate.now();
        return this;
    }

    public void clean() {
        localDate = new LocalDate(1970, 1, 1);
    }

    public boolean isClean() {
        return (localDate.getYear() == 1970 &&
                localDate.getMonthOfYear() == 1 &&
                localDate.getDayOfMonth() == 1);
    }

    public Integer getYear() {
        return localDate.getYear();
    }

    public Integer getMonth() {
        return localDate.getMonthOfYear() - 1;
    }

    public Integer getDay() {
        return localDate.getDayOfMonth();
    }

    public long toSeconds() {
        return toMillis() / 1000;
    }

    public long toMillis() {
        return localDate.toDateTime(LocalTime.MIDNIGHT).getMillis();
    }

    public Long toLong() {
        if (isClean()) {
            return null;
        }
        return toSeconds();
    }

    @NonNull
    public String toString() {
        return localDate.toString(DATE_FORMAT);
    }
}