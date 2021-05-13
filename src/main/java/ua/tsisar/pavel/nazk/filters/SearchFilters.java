package ua.tsisar.pavel.nazk.filters;

public class SearchFilters {

    private final FilterString query = new FilterString();
    private final FilterInteger userDeclarantId = new FilterInteger();
    private final DocumentType documentType = new DocumentType();
    private final DeclarationType declarationType = new DeclarationType();
    private final FilterInteger declarationYear = new FilterInteger();
    private final Period period = new Period();
    private final FilterInteger page = new FilterInteger();

    public FilterString query() {
        return query;
    }

    public FilterInteger userDeclarantId() {
        return userDeclarantId;
    }

    public DocumentType documentType() {
        return documentType;
    }

    public DeclarationType declarationType() {
        return declarationType;
    }

    public FilterInteger declarationYear() {
        return declarationYear;
    }

    public Period period() {
        return period;
    }

    public FilterInteger page() {
        return page;
    }

    public void clear() {
        query.clear();
        userDeclarantId.clear();
        documentType.clear();
        declarationType.clear();
        declarationYear.clear();
        period.clear();
        page.clear();
    }

    public boolean isClear() {
        return query.isClear() &&
                userDeclarantId.isClear() &&
                documentType.isClear() &&
                declarationType.isClear() &&
                declarationYear.isClear() &&
                period.isClear() &&
                page.isClear();
    }
}
