package com.project.laundryappui.api;

/**
 * ApiConfig - Cấu hình API endpoints
 */
public class ApiConfig {
    // Base URL của backend NestJS
    // Thay đổi URL này theo môi trường của bạn
    
    // ===== CHỌN 1 TRONG CÁC OPTION SAU =====
    
    // Option 1: Android Emulator (AVD)
    public static final String BASE_URL = "http://10.0.2.2:3001/";
    
    // Option 2: Real Device (cùng WiFi) - Uncomment dòng dưới và comment dòng trên
    // public static final String BASE_URL = "http://10.54.112.49:3001/";
    
    // Option 3: Production
    // public static final String BASE_URL = "https://your-domain.com/";
    
    // API Prefix từ NestJS (theo main.ts)
    public static final String API_PREFIX = "api/v1/";
    
    // API Endpoints - Auth
    public static final String AUTH_LOGIN = "auth/login";
    public static final String AUTH_REGISTER = "auth/register";
    public static final String AUTH_LOGOUT = "auth/logout";
    public static final String AUTH_REFRESH = "auth/refresh";
    public static final String AUTH_ME = "auth/me";
    public static final String AUTH_CHANGE_PASSWORD = "auth/change-password";
    
    // API Endpoints - Users
    public static final String USERS_PROFILE = "users/profile";
    public static final String USERS_ADDRESSES = "users/addresses";
    public static final String USERS_AVATAR = "users/avatar";
    
    // Timeouts
    public static final int CONNECT_TIMEOUT = 30; // seconds
    public static final int READ_TIMEOUT = 30; // seconds
    public static final int WRITE_TIMEOUT = 30; // seconds
}

