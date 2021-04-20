package ua.tsisar.pavel.nazk.filters;

public class SearchFilters {

    private final WrapperString query = new WrapperString();
    private final WrapperInteger userDeclarantId = new WrapperInteger();
    private final DocumentType documentType = new DocumentType();
    private final DeclarationType declarationType = new DeclarationType();
    private final WrapperInteger declarationYear = new WrapperInteger();
    private final Period period = new Period();
    private final WrapperInteger page = new WrapperInteger();

    public WrapperString query() {
        return query;
    }

    public WrapperInteger userDeclarantId() {
        return userDeclarantId;
    }

    public DocumentType documentType() {
        return documentType;
    }

    public DeclarationType declarationType() {
        return declarationType;
    }

    public WrapperInteger declarationYear() {
        return declarationYear;
    }

    public Period period() {
        return period;
    }

    public WrapperInteger page() {
        return page;
    }

    public void clean() {
        query.clean();
        userDeclarantId.clean();
        documentType.clean();
        declarationType.clean();
        declarationYear.clean();
        period.clean();
        page.clean();
    }

    public boolean isClean() {
        return query.isClean() && userDeclarantId.isClean() && documentType.isClean() &&
                declarationType.isClean() && declarationYear.isClean() && period.isClean() && page.isClean();
    }
}
