package ua.tsisar.pavel.nazk.filters;

public class WrapperString extends Wrapper<String> {

    public void set(String s) {
        if(s.isEmpty())
            super.set(null);
        else
            super.set(s);
    }
}
