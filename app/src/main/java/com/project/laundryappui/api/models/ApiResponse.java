package com.project.laundryappui.api.models;

/**
 * ApiResponse - Generic wrapper cho tất cả API responses từ backend
 * Backend trả về format: { success, data, timestamp }
 */
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String timestamp;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

