package ua.tsisar.pavel.nazk.filters;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DocumentType extends Wrapper<Integer> {
    @IntDef({DOCUMENT_ALL, DOCUMENT_DECLARATION, DOCUMENT_REPORT, DOCUMENT_NEW_DECLARATION, DOCUMENT_NEW_REPORT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Document {}

    public static final int DOCUMENT_ALL = 0;
    public static final int DOCUMENT_DECLARATION = 1;
    public static final int DOCUMENT_REPORT = 2;
    public static final int DOCUMENT_NEW_DECLARATION = 3;
    public static final int DOCUMENT_NEW_REPORT = 4;

    @Override
    public void set(@Document Integer i){
        super.set(i);
    }
}
