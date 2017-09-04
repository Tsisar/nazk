package ua.tsisar.pavel.nazk.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PageDTO {

    @SerializedName("batchSize")
    @Expose
    private Integer batchSize;
    @SerializedName("totalItems")
    @Expose
    private Integer totalItems;

    /**
     * No args constructor for use in serialization
     *
     */
    public PageDTO() {
    }

    /**
     *
     * @param totalItems
     * @param batchSize
     */
    public PageDTO(Integer batchSize, Integer totalItems) {
        super();
        this.batchSize = batchSize;
        this.totalItems = totalItems;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

