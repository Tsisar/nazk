package ua.com.tsisar.nazk.filters;

public class Wrapper<V> {
    private V value;

    public V get() {
        return value;
    }

    public void set(V value) {
        if(value instanceof Number && (Integer) value == 0) {
            this.value = null;
        }else if(value instanceof String && ((String) value).isEmpty()) {
            this.value = null;
        }else {
            this.value = value;
        }
    }

    public void clear(){
        value = null;
    }

    public boolean isClear(){
        return value == null;
    }

}
