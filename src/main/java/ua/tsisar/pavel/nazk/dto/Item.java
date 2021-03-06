package ua.tsisar.pavel.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("data")
    @Expose
    private ItemData data;
    @SerializedName("type")
    @Expose
    private Integer documentType;
    @SerializedName("declaration_type")
    @Expose
    private Integer declarationType;
    @SerializedName("declaration_year")
    @Expose
    private Integer declarationYear;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_declarant_id")
    @Expose
    private Integer userDeclarantId;
    @SerializedName("date")
    @Expose
    private String date;

    public Item(ItemData data, Integer documentType, Integer declarationType,
                Integer declarationYear, String id, Integer userDeclarantId, String date) {
        this.data = data;
        this.documentType = documentType;
        this.declarationType = declarationType;
        this.declarationYear = declarationYear;
        this.id = id;
        this.userDeclarantId = userDeclarantId;
        this.date = date;
    }

    public ItemData getData() {
        return data;
    }

    public void setData(ItemData data) {
        this.data = data;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public Integer getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(Integer declarationType) {
        this.declarationType = declarationType;
    }

    public Integer getDeclarationYear() {
        return declarationYear;
    }

    public void setDeclarationYear(Integer declarationYear) {
        this.declarationYear = declarationYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserDeclarantId() {
        return userDeclarantId;
    }

    public void setUserDeclarantId(Integer userDeclarantId) {
        this.userDeclarantId = userDeclarantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Item{" +
                "data=" + data +
                ", documentType=" + documentType +
                ", declarationType=" + declarationType +
                ", declarationYear=" + declarationYear +
                ", id='" + id + '\'' +
                ", userDeclarantId=" + userDeclarantId +
                ", date='" + date + '\'' +
                '}';
    }
}

