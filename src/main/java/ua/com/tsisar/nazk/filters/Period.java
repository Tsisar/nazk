package ua.com.tsisar.nazk.filters;

import ua.com.tsisar.nazk.util.Date;

public class Period {
    private final Date startDate = new Date();
    private final Date endDate = new Date();

    public Date startDate() {
        return startDate;
    }

    public Date endDate() {
        return endDate;
    }

    public void clear(){
        startDate.clear();
        endDate.clear();
    }

    public boolean isClear() {
        return startDate.isClear() && endDate.isClear();
    }

}
