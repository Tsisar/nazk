package ua.tsisar.pavel.nazk.filters;

public class Wrapper<V>{
    private V value;

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }

    public void clean() {
        value = null;
    }

    public boolean isClean() {
        return value == null;
    }

}
