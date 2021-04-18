package ua.tsisar.pavel.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step1 {

    @SerializedName("data")
    @Expose
    private Step1Data data;

    public Step1(Step1Data data) {
        this.data = data;
    }

    public Step1Data getData() {
        return data;
    }

    public void setData(Step1Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Step1{" +
                "data=" + data +
                '}';
    }
}
