package ua.com.tsisar.nazk.search;

import android.os.Bundle;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ua.com.tsisar.nazk.search.listener.SearchFiltersListener;
import ua.com.tsisar.nazk.search.constants.SearchFiltersConstants;


public class SearchFilters {
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
    private int userDeclarantId = 0;
    private int documentType = 0;
    private int declarationType = 0;
    private int declarationYear = 0;
    private String startDate = "";
    private String endDate = "";
    private int page = 0;

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

    public SearchFilters setUserDeclarantId(int userDeclarantId) {
        this.userDeclarantId = userDeclarantId;
        return this;
    }

    public int getUserDeclarantId() {
        return userDeclarantId;
    }

    public SearchFilters setDocumentType(@Document int documentType) {
        this.documentType = documentType;
        return this;
    }

    public int getDocumentType() {
        return documentType;
    }

    public SearchFilters setDeclarationType(@Declaration int declarationType) {
        this.declarationType = declarationType;
        return this;
    }

    public int getDeclarationType() {
        return declarationType;
    }

    public SearchFilters setDeclarationYear(int declarationYear) {
        this.declarationYear = declarationYear;
        return this;
    }

    public int getDeclarationYear() {
        return declarationYear;
    }

    public String getStartDate() {
        return startDate;
    }

    public SearchFilters setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public SearchFilters setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getPage() {
        return page;
    }

    public SearchFilters setPage(int page) {
        this.page = page;
        return this;
    }

    public boolean isNull(){
        return query.length() == 0 &&
                userDeclarantId == 0 &&
                documentType == 0 &&
                declarationType == 0 &&
                declarationYear == 0 &&
                startDate.length() == 0 &&
                endDate.length() == 0 &&
                page == 0;
    }

    public void update(){
        listener.onUpdateSearchFilters();
    }

    public void save(Bundle outState){
        outState.putString(SearchFiltersConstants.EXTRA_QUERY, query);
        outState.putInt(SearchFiltersConstants.EXTRA_USER_DECLARANT_ID, userDeclarantId);
        outState.putInt(SearchFiltersConstants.EXTRA_DOCUMENT_TYPE, documentType);
        outState.putInt(SearchFiltersConstants.EXTRA_DECLARATION_TYPE, declarationType);
        outState.putInt(SearchFiltersConstants.EXTRA_DECLARATION_YEAR, declarationYear);
        outState.putString(SearchFiltersConstants.EXTRA_START_DATE, startDate);
        outState.putString(SearchFiltersConstants.EXTRA_END_DATE, endDate);
        outState.putInt(SearchFiltersConstants.EXTRA_PAGE, page);
    }

    public SearchFilters restore(Bundle savedInstanceState){
        query = savedInstanceState.getString(SearchFiltersConstants.EXTRA_QUERY, "");
        userDeclarantId = savedInstanceState.getInt(SearchFiltersConstants.EXTRA_USER_DECLARANT_ID, 0);
        documentType = savedInstanceState.getInt(SearchFiltersConstants.EXTRA_DOCUMENT_TYPE, 0);
        declarationType = savedInstanceState.getInt(SearchFiltersConstants.EXTRA_DECLARATION_TYPE, 0);
        declarationYear = savedInstanceState.getInt(SearchFiltersConstants.EXTRA_DECLARATION_YEAR, 0);
        startDate = savedInstanceState.getString(SearchFiltersConstants.EXTRA_START_DATE, "");
        endDate = savedInstanceState.getString(SearchFiltersConstants.EXTRA_END_DATE, "");
        page = savedInstanceState.getInt(SearchFiltersConstants.EXTRA_PAGE, 0);
        return this;
    }
}
