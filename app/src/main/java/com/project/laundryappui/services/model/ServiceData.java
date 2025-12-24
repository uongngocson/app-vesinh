package com.project.laundryappui.services.model;

import java.util.List;

public class ServiceData {
    private List<LaundryService> services;
    private String version;
    private String last_updated;
    
    public List<LaundryService> getServices() {
        return services;
    }
    
    public void setServices(List<LaundryService> services) {
        this.services = services;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getLastUpdated() {
        return last_updated;
    }
    
    public void setLastUpdated(String last_updated) {
        this.last_updated = last_updated;
    }
    
    public LaundryService getServiceById(String serviceId) {
        if (services != null) {
            for (LaundryService service : services) {
                if (service.getId().equals(serviceId)) {
                    return service;
                }
            }
        }
        return null;
    }
}

