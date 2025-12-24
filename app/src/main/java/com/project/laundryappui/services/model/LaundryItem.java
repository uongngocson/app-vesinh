package com.project.laundryappui.services.model;

public class LaundryItem {
    private String id;
    private String name;
    private String name_key;
    private int price;
    private String unit;
    private String unit_key;
    private String estimated_time;
    private String estimated_time_key;
    private String icon;
    private String category;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNameKey() {
        return name_key;
    }
    
    public void setNameKey(String name_key) {
        this.name_key = name_key;
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getUnitKey() {
        return unit_key;
    }
    
    public void setUnitKey(String unit_key) {
        this.unit_key = unit_key;
    }
    
    public String getEstimatedTime() {
        return estimated_time;
    }
    
    public void setEstimatedTime(String estimated_time) {
        this.estimated_time = estimated_time;
    }
    
    public String getEstimatedTimeKey() {
        return estimated_time_key;
    }
    
    public void setEstimatedTimeKey(String estimated_time_key) {
        this.estimated_time_key = estimated_time_key;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
}

