package ua.com.tsisar.nazk.search;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ua.com.tsisar.nazk.util.Date;

public class SearchFilters {
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
    private Date startDate;
    private Date endDate;
    private int page;

    public SearchFilters() {
        this.startDate = new Date();
        this.endDate = new Date();
    }

    public SearchFilters set(String query, int userDeclarantId, @Document int documentType,
                         @Declaration int declarationType, int declarationYear, Date startDate,
                         Date endDate, int page) {
        this.query = query;
        this.userDeclarantId = userDeclarantId;
        this.documentType = documentType;
        this.declarationType = declarationType;
        this.declarationYear = declarationYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setUserDeclarantId(int userDeclarantId) {
        this.userDeclarantId = userDeclarantId;
    }

    public void setDocumentType(@Document int documentType) {
        this.documentType = documentType;
    }

    public void setDeclarationType(@Declaration int declarationType) {
        this.declarationType = declarationType;
    }

    public void setDeclarationYear(int declarationYear) {
        this.declarationYear = declarationYear;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public int getUserDeclarantId() {
        return userDeclarantId;
    }

    public int getDocumentType() {
        return documentType;
    }

    public int getDeclarationType() {
        return declarationType;
    }

    public int getDeclarationYear() {
        return declarationYear;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getPage() {
        return page;
    }
}
