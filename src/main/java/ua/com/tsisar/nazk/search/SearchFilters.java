package ua.com.tsisar.nazk.search;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SearchFilters {
    private static final String TAG = "MyLog";

    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_USER_DECLARANT_ID = "user_declarant_id";
    public static final String EXTRA_DOCUMENT_TYPE = "document_type";
    public static final String EXTRA_DECLARATION_TYPE = "declaration_type";
    public static final String EXTRA_DECLARATION_YEAR = "declaration_year";
    public static final String EXTRA_DT_START = "start_date";
    public static final String EXTRA_DT_END = "end_date";
    public static final String EXTRA_PAGE = "page";

    @IntDef({DOCUMENT_ALL, DOCUMENT_DECLARATION, DOCUMENT_REPORT, DOCUMENT_NEW_DECLARATION, DOCUMENT_NEW_REPORT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Document {}

    public static final int DOCUMENT_ALL = 0;
    public static final int DOCUMENT_DECLARATION = 1;
    public static final int DOCUMENT_REPORT = 2;
    public static final int DOCUMENT_NEW_DECLARATION = 3;
    public static final int DOCUMENT_NEW_REPORT = 4;

    @IntDef({DECLARATION_ALL, DECLARATION_ANNUAL, DECLARATION_BEFORE_DISMISSAL, DECLARATION_AFTER_DISMISSAL, DECLARATION_CANDIDATES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Declaration {}

    public static final int DECLARATION_ALL = 0;
    public static final int DECLARATION_ANNUAL = 1;
    public static final int DECLARATION_BEFORE_DISMISSAL = 2;
    public static final int DECLARATION_AFTER_DISMISSAL = 3;
    public static final int DECLARATION_CANDIDATES = 4;

    private String query;
    private int userDeclarantId;
    private int documentType;
    private int declarationType;
    private int declarationYear;
    private String startDate;
    private String endDate;
    private int page;

    public SearchFilters(String query, int userDeclarantId,@Document int documentType,
                         @Declaration int declarationType, int declarationYear, String startDate,
                         String endDate, int page) {
        this.query = query;
        this.userDeclarantId = userDeclarantId;
        this.documentType = documentType;
        this.declarationType = declarationType;
        this.declarationYear = declarationYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getUserDeclarantId() {
        return userDeclarantId;
    }

    public void setUserDeclarantId(int userDeclarantId) {
        this.userDeclarantId = userDeclarantId;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(@Document int documentType) {
        this.documentType = documentType;
    }

    public int getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(@Declaration int declarationType) {
        this.declarationType = declarationType;
    }

    public int getDeclarationYear() {
        return declarationYear;
    }

    public void setDeclarationYear(int declarationYear) {
        this.declarationYear = declarationYear;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
