package com.project.laundryappui.api.models;

import java.util.List;

/**
 * ApiError - Model cho error response từ API
 * Map với HttpExceptionFilter từ NestJS backend
 */
public class ApiError {
    private int statusCode;
    private String message;
    private String error;
    private List<String> messages;
    private String timestamp;
    private String path;

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Lấy message đầu tiên từ danh sách messages hoặc message chính
     */
    public String getDisplayMessage() {
        if (messages != null && !messages.isEmpty()) {
            return messages.get(0);
        }
        return message != null ? message : "Có lỗi xảy ra";
    }
}

