package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerDTO {

    @SerializedName("data")
    @Expose
    private List<ItemDTO> data = null;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("notice")
    @Expose
    private String notice;

    public List<ItemDTO> getData() {
        return data;
    }

    public void setData(List<ItemDTO> data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
