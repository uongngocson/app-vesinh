package com.project.laundryappui.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * CleaningBookingRequest - Model cho request body của API cleaning-booking
 */
public class CleaningBookingRequest {

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("customerPhone")
    private String customerPhone;

    @SerializedName("customerEmail")
    private String customerEmail;

    @SerializedName("shippingAddress")
    private Address shippingAddress;

    @SerializedName("billingAddress")
    private Address billingAddress;

    @SerializedName("itemDescription")
    private String itemDescription;

    @SerializedName("itemPhotos")
    private List<String> itemPhotos;

    @SerializedName("serviceNotes")
    private String serviceNotes;

    @SerializedName("notes")
    private String notes;

    @SerializedName("metadata")
    private Map<String, Object> metadata;

    // Constructors
    public CleaningBookingRequest() {}

    public CleaningBookingRequest(String customerName, String customerPhone) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public List<String> getItemPhotos() {
        return itemPhotos;
    }

    public void setItemPhotos(List<String> itemPhotos) {
        this.itemPhotos = itemPhotos;
    }

    public String getServiceNotes() {
        return serviceNotes;
    }

    public void setServiceNotes(String serviceNotes) {
        this.serviceNotes = serviceNotes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
