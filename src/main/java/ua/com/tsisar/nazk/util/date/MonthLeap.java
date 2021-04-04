package ua.com.tsisar.nazk.util.date;

public enum MonthLeap implements Month {
    JANUARY(31),
    FEBRUARY(29),
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

    MonthLeap(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static MonthLeap forMonth(int month) {
        return MonthLeap.values()[month];
    }
}
