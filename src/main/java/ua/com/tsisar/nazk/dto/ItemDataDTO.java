package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDataDTO {

    @SerializedName("step_1")
    @Expose
    private Step1DTO step1;

    public Step1DTO getStep1() {
        return step1;
    }

    public void setStep1(Step1DTO step1) {
        this.step1 = step1;
    }

}
