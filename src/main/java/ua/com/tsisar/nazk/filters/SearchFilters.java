package ua.com.tsisar.nazk.filters;

import ua.com.tsisar.nazk.util.Date;

public class SearchFilters {

    private final Wrapper<String> query = new Wrapper<>();
    private final Wrapper<Integer> userDeclarantId = new Wrapper<>();
    private final DocumentType documentType = new DocumentType();
    private final DeclarationType declarationType = new DeclarationType();
    private final Wrapper<Integer> declarationYear = new Wrapper<>();
    private final Date startDate = new Date();
    private final Date endDate = new Date();
    private final Wrapper<Integer> page = new Wrapper<>();

    public Wrapper<String> query() {
        return query;
    }

    public Wrapper<Integer> userDeclarantId() {
        return userDeclarantId;
    }

    public DocumentType documentType() {
        return documentType;
    }

    public DeclarationType declarationType() {
        return declarationType;
    }

    public Wrapper<Integer> declarationYear() {
        return declarationYear;
    }

    public Date startDate() {
        return startDate;
    }

    public Date endDate() {
        return endDate;
    }

    public Wrapper<Integer> page() {
        return page;
    }
}
