package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step1DTO {

    @SerializedName("data")
    @Expose
    private Step1DataDTO data;

    public Step1DataDTO getData() {
        return data;
    }

    public void setData(Step1DataDTO data) {
        this.data = data;
    }

}
