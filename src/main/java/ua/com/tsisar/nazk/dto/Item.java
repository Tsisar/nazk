package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("data")
    @Expose
    private ItemData data;
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

    public ItemData getData() {
        return data;
    }

    public void setData(ItemData data) {
        this.data = data;
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

    //testing

    public String getLastname() {
        return data.getStep1().getData().getLastname();
    }

    public String getFirstname() {
        return data.getStep1().getData().getFirstname();
    }

    public String getMiddlename() {
        return data.getStep1().getData().getMiddlename();
    }

    public String getWorkPost() {
        return data.getStep1().getData().getWorkPost();
    }

    public String getWorkPlace() {
        return data.getStep1().getData().getWorkPlace();
    }
}
