package ua.com.tsisar.nazk.filters;

public class SearchFilters {

    private final Wrapper<String> query = new Wrapper<>();
    private final Wrapper<Integer> userDeclarantId = new Wrapper<>();
    private final DocumentType documentType = new DocumentType();
    private final DeclarationType declarationType = new DeclarationType();
    private final Wrapper<Integer> declarationYear = new Wrapper<>();
    private final Period period = new Period();
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

    public Period period(){
        return period;
    }

    public Wrapper<Integer> page() {
        return page;
    }

    public void clear(){
        query.clear();
        userDeclarantId.clear();
        documentType.clear();
        declarationType.clear();
        declarationYear.clear();
        period.clear();
        page.clear();
    }
}
