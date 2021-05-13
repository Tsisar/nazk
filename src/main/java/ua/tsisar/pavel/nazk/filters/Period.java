package ua.tsisar.pavel.nazk.filters;

import ua.tsisar.pavel.nazk.util.Date;

public class Period {
    private final Date startDate = new Date();
    private final Date endDate = new Date();

    public Date startDate() {
        return startDate;
    }

    public Date endDate() {
        return endDate;
    }

    public void clear() {
        startDate.clean();
        endDate.clean();
    }

    public boolean isClear() {
        return startDate.isClean() && endDate.isClean();
    }

}
