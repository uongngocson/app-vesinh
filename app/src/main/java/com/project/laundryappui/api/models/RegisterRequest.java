package com.project.laundryappui.api.models;

/**
 * RegisterRequest - DTO cho API đăng ký
 * Map với RegisterDto từ NestJS backend
 */
public class RegisterRequest {
    private String email;
    private String phone;
    private String password;
    private String fullName;

    public RegisterRequest(String email, String phone, String password, String fullName) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

