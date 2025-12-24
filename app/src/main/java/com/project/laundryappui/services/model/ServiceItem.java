package com.project.laundryappui.services.model;

public class ServiceItem {
    private String id;
    private String name;
    private int price;
    private String estimatedTime;
    private int iconResource;
    private int quantity;
    private String category;
    
    public ServiceItem(String name, int price, String estimatedTime, int iconResource) {
        this.name = name;
        this.price = price;
        this.estimatedTime = estimatedTime;
        this.iconResource = iconResource;
        this.quantity = 0;
    }
    
    // Constructor from LaundryItem
    public ServiceItem(LaundryItem item, int iconResource) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.estimatedTime = item.getEstimatedTime();
        this.iconResource = iconResource;
        this.quantity = 0;
        this.category = item.getCategory();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public String getEstimatedTime() {
        return estimatedTime;
    }
    
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    public int getIconResource() {
        return iconResource;
    }
    
    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

