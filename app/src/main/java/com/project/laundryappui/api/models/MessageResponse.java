package com.project.laundryappui.api.models;

/**
 * MessageResponse - Response chỉ chứa message
 * Dùng cho logout, change password, etc.
 */
public class MessageResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

