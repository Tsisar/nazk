package ua.tsisar.pavel.nazk.search;

import android.os.Bundle;
import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ua.tsisar.pavel.nazk.search.listener.SearchFiltersListener;

public class SearchFilters {
    private static final String EXTRA_QUERY = "query";
    private static final String EXTRA_DECLARATION_YEAR = "DeclarationYear";
    private static final String EXTRA_DECLARATION_TYPE = "DeclarationType";
    private static final String EXTRA_DOCUMENT_TYPE = "DocumentType";
    private static final String EXTRA_DT_START = "dtStart";
    private static final String EXTRA_DT_END = "dtEnd";

    @IntDef({DECLARATION_ALL, DECLARATION_ANNUAL, DECLARATION_BEFORE_DISMISSAL, DECLARATION_AFTER_DISMISSAL, DECLARATION_CANDIDATES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Declaration {}

    public static final int DECLARATION_ALL = 0;
    public static final int DECLARATION_ANNUAL = 1;
    public static final int DECLARATION_BEFORE_DISMISSAL = 2;
    public static final int DECLARATION_AFTER_DISMISSAL = 3;
    public static final int DECLARATION_CANDIDATES = 4;

    @IntDef({DOCUMENT_ALL, DOCUMENT_DECLARATION, DOCUMENT_REPORT, DOCUMENT_NEW_DECLARATION, DOCUMENT_NEW_REPORT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Document {}

    public static final int DOCUMENT_ALL = 0;
    public static final int DOCUMENT_DECLARATION = 1;
    public static final int DOCUMENT_REPORT = 2;
    public static final int DOCUMENT_NEW_DECLARATION = 3;
    public static final int DOCUMENT_NEW_REPORT = 4;

    private String query = "";
    private int declarationYear = 0;
    private int declarationType = 0;
    private int documentType = 0;
    private String dtStart = "";
    private String dtEnd = "";

    private SearchFiltersListener listener;

    public SearchFilters(SearchFiltersListener listener){
        this.listener = listener;
    }

    public SearchFilters setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public SearchFilters setDeclarationYear(int declarationYear) {
        this.declarationYear = declarationYear;
        return this;
    }

    public int getDeclarationYear() {
        return declarationYear;
    }

    public SearchFilters setDeclarationType(@Declaration int declarationType) {
        this.declarationType = declarationType;
        return this;
    }

    public int getDeclarationType() {
        return declarationType;
    }

    public SearchFilters setDocumentType(@Document int documentType) {
        this.documentType = documentType;
        return this;
    }

    public int getDocumentType() {
        return documentType;
    }

    public String getDtStart() {
        return dtStart;
    }

    public SearchFilters setDtStart(String dtStart) {
        this.dtStart = dtStart;
        return this;
    }

    public String getDtEnd() {
        return dtEnd;
    }

    public SearchFilters setDtEnd(String dtEnd) {
        this.dtEnd = dtEnd;
        return this;
    }

    public boolean isNull(){
        return query.length() == 0 &&
                declarationYear == 0 &&
                declarationType == 0 &&
                documentType == 0 &&
                dtStart.length() == 0 &&
                dtEnd.length() == 0;
    }

    public void update(){
        listener.onUpdateSearchFilters();
    }

    public void save(Bundle outState){
        outState.putString(EXTRA_QUERY, query);
        outState.putInt(EXTRA_DECLARATION_YEAR, declarationYear);
        outState.putInt(EXTRA_DECLARATION_TYPE, declarationType);
        outState.putInt(EXTRA_DOCUMENT_TYPE, documentType);
        outState.putString(EXTRA_DT_START, dtStart);
        outState.putString(EXTRA_DT_END, dtEnd);
    }

    public SearchFilters restore(Bundle savedInstanceState){
        query = savedInstanceState.getString(EXTRA_QUERY, "");
        declarationYear = savedInstanceState.getInt(EXTRA_DECLARATION_YEAR, 0);
        declarationType = savedInstanceState.getInt(EXTRA_DECLARATION_TYPE, 0);
        documentType = savedInstanceState.getInt(EXTRA_DOCUMENT_TYPE, 0);
        dtStart = savedInstanceState.getString(EXTRA_DT_START, "");
        dtEnd = savedInstanceState.getString(EXTRA_DT_END, "");
        return this;
    }
}
