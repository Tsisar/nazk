package ua.tsisar.pavel.nazk.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AnswerDTO {

    @SerializedName("page")
    @Expose
    private PageDTO page;
    @SerializedName("items")
    @Expose
    private List<ItemDTO> items = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public AnswerDTO() {
    }

    /**
     *
     * @param items
     * @param page
     */
    public AnswerDTO(PageDTO page, List<ItemDTO> items) {
        super();
        this.page = page;
        this.items = items;
    }

    public PageDTO getPage() {
        return page;
    }

    public void setPage(PageDTO page) {
        this.page = page;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}