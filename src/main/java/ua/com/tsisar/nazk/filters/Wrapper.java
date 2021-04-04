package ua.com.tsisar.nazk.filters;

public class Wrapper<V> {
    private V value;

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }

    public void clear(){
        value = null;
    }

    public boolean isClear(){
        return value == null;
    }

}
