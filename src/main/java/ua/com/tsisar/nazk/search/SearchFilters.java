package ua.com.tsisar.nazk.search;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SearchFilters {
    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_USER_DECLARANT_ID = "user_declarant_id";
    public static final String EXTRA_DOCUMENT_TYPE = "document_type";
    public static final String EXTRA_DECLARATION_TYPE = "declaration_type";
    public static final String EXTRA_DECLARATION_YEAR = "declaration_year";
    public static final String EXTRA_DT_START = "start_date";
    public static final String EXTRA_DT_END = "end_date";
    public static final String EXTRA_PAGE = "page";

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

    public SearchFilters(String query, int userDeclarantId, int documentType, int declarationType,
                         int declarationYear, String startDate, String endDate, int page) {
        this.query = query;
        this.userDeclarantId = userDeclarantId;
        this.documentType = documentType;
        this.declarationType = declarationType;
        this.declarationYear = declarationYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;
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
}
