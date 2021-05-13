package ua.tsisar.pavel.nazk.filters;

public class Filter<V>{
    private V value;

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }

    public void clear() {
        value = null;
    }

    public boolean isClear() {
        return value == null;
    }

}
