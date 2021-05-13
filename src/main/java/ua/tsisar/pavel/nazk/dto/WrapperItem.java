package ua.tsisar.pavel.nazk.dto;

public class WrapperItem {
    private final Item item;

    public WrapperItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Integer getDocumentType() {
        return item.getDocumentType();
    }

    public Integer getDeclarationType() {
        return item.getDeclarationType();
    }

    public Integer getDeclarationYear() {
        return item.getDeclarationYear();
    }

    public String getId() {
        return item.getId();
    }

    public Integer getUserDeclarantId() {
        return item.getUserDeclarantId();
    }

    public String getDate() {
        return item.getDate();
    }

    public String getLastName() {
        return item.getData().getStep1().getData().getLastname();
    }

    public String getFirstName() {
        return item.getData().getStep1().getData().getFirstname();
    }

    public String getMiddleName() {
        return item.getData().getStep1().getData().getMiddlename();
    }

    public String getWorkPost() {
        return item.getData().getStep1().getData().getWorkPost();
    }

    public String getWorkPlace() {
        return item.getData().getStep1().getData().getWorkPlace();
    }
}
