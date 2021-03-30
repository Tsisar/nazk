package ua.com.tsisar.nazk.util;

public enum M_LEAP implements Month {
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

    M_LEAP(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static M_LEAP forMonth(int month) {
        return M_LEAP.values()[month];
    }
}
