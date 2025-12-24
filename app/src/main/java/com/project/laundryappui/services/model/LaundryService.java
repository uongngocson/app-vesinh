package com.project.laundryappui.services.model;

import java.util.List;

public class LaundryService {
    private String id;
    private String name;
    private String name_key;
    private String description;
    private String description_key;
    private String icon;
    private String estimated_time;
    private String estimated_time_key;
    private String express_time;
    private String express_time_key;
    private List<ServiceFeature> features;
    private List<LaundryItem> items;
    
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescriptionKey() {
        return description_key;
    }
    
    public void setDescriptionKey(String description_key) {
        this.description_key = description_key;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
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
    
    public String getExpressTime() {
        return express_time;
    }
    
    public void setExpressTime(String express_time) {
        this.express_time = express_time;
    }
    
    public String getExpressTimeKey() {
        return express_time_key;
    }
    
    public void setExpressTimeKey(String express_time_key) {
        this.express_time_key = express_time_key;
    }
    
    public List<ServiceFeature> getFeatures() {
        return features;
    }
    
    public void setFeatures(List<ServiceFeature> features) {
        this.features = features;
    }
    
    public List<LaundryItem> getItems() {
        return items;
    }
    
    public void setItems(List<LaundryItem> items) {
        this.items = items;
    }
}

