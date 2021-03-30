package ua.com.tsisar.nazk.util;

public enum YEAR {
    TYPICAL (365),
    LEAP (366);
    private final int days;

    YEAR(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static YEAR forYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return LEAP;
        }
        return TYPICAL;
    }

    public Month forMonth(int month){
        if(this == YEAR.LEAP){
            return M_LEAP.forMonth(month);
        }
        return M_TYPICAL.forMonth(month);
    }
}