package ua.tsisar.pavel.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemData {

    @SerializedName("step_1")
    @Expose
    private Step1 step1;

    public ItemData(Step1 step1) {
        this.step1 = step1;
    }

    public Step1 getStep1() {
        return step1;
    }

    public void setStep1(Step1 step1) {
        this.step1 = step1;
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "step1=" + step1 +
                '}';
    }
}
