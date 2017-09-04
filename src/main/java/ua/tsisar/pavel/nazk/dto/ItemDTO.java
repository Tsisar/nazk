package ua.tsisar.pavel.nazk.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ItemDTO {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("placeOfWork")
    @Expose
    private String placeOfWork;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("linkPDF")
    @Expose
    private String linkPDF;

    /**
     * No args constructor for use in serialization
     *
     */
    public ItemDTO() {
    }

    /**
     *
     * @param position
     * @param placeOfWork
     * @param id
     * @param linkPDF
     * @param lastname
     * @param firstname
     */
    public ItemDTO(String id, String firstname, String lastname, String placeOfWork, String position, String linkPDF) {
        super();
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.placeOfWork = placeOfWork;
        this.position = position;
        this.linkPDF = linkPDF;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLinkPDF() {
        return linkPDF;
    }

    public void setLinkPDF(String linkPDF) {
        this.linkPDF = linkPDF;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}