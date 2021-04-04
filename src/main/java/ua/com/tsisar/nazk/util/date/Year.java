package ua.com.tsisar.nazk.util.date;

public enum Year {
    TYPICAL (365),
    LEAP (366);
    private final int days;

    Year(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static Year forYear(int year) {
        return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? LEAP : TYPICAL;
    }

    public Month forMonth(int month){
        return this == Year.LEAP ? MonthLeap.forMonth(month) : MonthTypical.forMonth(month);
    }
}
