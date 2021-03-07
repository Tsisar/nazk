
package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDTO {

    @SerializedName("data")
    @Expose
    private ItemDataDTO data;
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

    public ItemDataDTO getData() {
        return data;
    }

    public void setData(ItemDataDTO data) {
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

}
