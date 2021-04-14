package ua.com.tsisar.nazk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Answer {

    @SerializedName("data")
    @Expose
    private List<Item> data = null;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("notice")
    @Expose
    private String notice;
    @SerializedName("error")
    @Expose
    private Integer error;

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
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

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "data=" + data +
                ", count=" + count +
                ", notice='" + notice + '\'' +
                ", error=" + error +
                '}';
    }
}
