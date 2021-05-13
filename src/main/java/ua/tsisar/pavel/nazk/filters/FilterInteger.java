package ua.tsisar.pavel.nazk.filters;

public class FilterInteger extends Filter<Integer> {

    public void set(Integer i) {
        if(i == 0)
            super.set(null);
        else
            super.set(i);
    }
}
