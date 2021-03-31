package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step1 {

    @SerializedName("data")
    @Expose
    private Step1Data data;

    public Step1Data getData() {
        return data;
    }

    public void setData(Step1Data data) {
        this.data = data;
    }

}
