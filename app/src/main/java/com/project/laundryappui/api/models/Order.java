package com.project.laundryappui.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Order - Model cho đơn hàng
 */
public class Order {

    @SerializedName("id")
    private String id;

    @SerializedName("orderNumber")
    private String orderNumber;

    @SerializedName("userId")
    private String userId;

    @SerializedName("storeId")
    private String storeId;

    @SerializedName("businessId")
    private int businessId;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("paymentStatus")
    private String paymentStatus;

    @SerializedName("subtotal")
    private String subtotal;

    @SerializedName("discountAmount")
    private String discountAmount;

    @SerializedName("taxAmount")
    private String taxAmount;

    @SerializedName("shippingFee")
    private String shippingFee;

    @SerializedName("totalAmount")
    private String totalAmount;

    @SerializedName("paidAmount")
    private String paidAmount;

    @SerializedName("refundedAmount")
    private String refundedAmount;

    @SerializedName("membershipDiscount")
    private String membershipDiscount;

    @SerializedName("pointsEarned")
    private int pointsEarned;

    @SerializedName("pointsUsed")
    private int pointsUsed;

    @SerializedName("couponCodes")
    private Object couponCodes;

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

    @SerializedName("estimatedCompletionDate")
    private Date estimatedCompletionDate;

    @SerializedName("actualCompletionDate")
    private Date actualCompletionDate;

    @SerializedName("itemDescription")
    private String itemDescription;

    @SerializedName("itemPhotos")
    private List<String> itemPhotos;

    @SerializedName("serviceNotes")
    private String serviceNotes;

    @SerializedName("assignedTo")
    private String assignedTo;

    @SerializedName("qualityCheckedBy")
    private String qualityCheckedBy;

    @SerializedName("completedBy")
    private String completedBy;

    @SerializedName("confirmedAt")
    private Date confirmedAt;

    @SerializedName("processingAt")
    private Date processingAt;

    @SerializedName("completedAt")
    private Date completedAt;

    @SerializedName("cancelledAt")
    private Date cancelledAt;

    @SerializedName("cancellationReason")
    private String cancellationReason;

    @SerializedName("notes")
    private String notes;

    @SerializedName("internalNotes")
    private String internalNotes;

    @SerializedName("metadata")
    private Object metadata;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("created_at")
    private Date createdAtDuplicate;

    @SerializedName("updated_at")
    private Date updatedAtDuplicate;

    @SerializedName("items")
    private List<Object> items;

    @SerializedName("payments")
    private List<Object> payments;

    @SerializedName("user")
    private User user;

    @SerializedName("store")
    private Store store;

    @SerializedName("business")
    private Business business;

    // Constructors
    public Order() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(String refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public String getMembershipDiscount() {
        return membershipDiscount;
    }

    public void setMembershipDiscount(String membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public Object getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(Object couponCodes) {
        this.couponCodes = couponCodes;
    }

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

    public Date getEstimatedCompletionDate() {
        return estimatedCompletionDate;
    }

    public void setEstimatedCompletionDate(Date estimatedCompletionDate) {
        this.estimatedCompletionDate = estimatedCompletionDate;
    }

    public Date getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(Date actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getQualityCheckedBy() {
        return qualityCheckedBy;
    }

    public void setQualityCheckedBy(String qualityCheckedBy) {
        this.qualityCheckedBy = qualityCheckedBy;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public Date getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Date confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Date getProcessingAt() {
        return processingAt;
    }

    public void setProcessingAt(Date processingAt) {
        this.processingAt = processingAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAtDuplicate() {
        return createdAtDuplicate;
    }

    public void setCreatedAtDuplicate(Date createdAtDuplicate) {
        this.createdAtDuplicate = createdAtDuplicate;
    }

    public Date getUpdatedAtDuplicate() {
        return updatedAtDuplicate;
    }

    public void setUpdatedAtDuplicate(Date updatedAtDuplicate) {
        this.updatedAtDuplicate = updatedAtDuplicate;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public List<Object> getPayments() {
        return payments;
    }

    public void setPayments(List<Object> payments) {
        this.payments = payments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
