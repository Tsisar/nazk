package ua.com.tsisar.nazk.util;

public enum M_TYPICAL implements Month {
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

    M_TYPICAL(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static M_TYPICAL forMonth(int month) {
        return M_TYPICAL.values()[month];
    }
}
