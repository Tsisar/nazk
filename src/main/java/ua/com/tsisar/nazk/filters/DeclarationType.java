package ua.com.tsisar.nazk.filters;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DeclarationType extends Wrapper<Integer> {
    @IntDef({DECLARATION_ALL, DECLARATION_ANNUAL, DECLARATION_BEFORE_DISMISSAL, DECLARATION_AFTER_DISMISSAL, DECLARATION_CANDIDATES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Declaration {}

    public static final int DECLARATION_ALL = 0;
    public static final int DECLARATION_ANNUAL = 1;
    public static final int DECLARATION_BEFORE_DISMISSAL = 2;
    public static final int DECLARATION_AFTER_DISMISSAL = 3;
    public static final int DECLARATION_CANDIDATES = 4;

    @Override
    public void set(@Declaration Integer i){
        super.set(i);
    }
}
