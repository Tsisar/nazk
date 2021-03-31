package ua.com.tsisar.nazk.util;

public enum MonthTypical implements Month {
    JANUARY(31),
    FEBRUARY(28),
    MARCH(31),
    APRIL(30),
    MAY(31),
    JUNE(30),
    JULY(31),
    AUGUST(31),
    SEPTEMBER(30),
    OCTOBER(31),
    NOVEMBER(30),
    DECEMBER(31);
    private final int days;

    MonthTypical(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static MonthTypical forMonth(int month) {
        return MonthTypical.values()[month];
    }
}
