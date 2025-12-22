package com.project.laundryappui.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SessionManager - Quản lý phiên đăng nhập người dùng
 * Sử dụng SharedPreferences để lưu trữ thông tin đăng nhập
 * Dễ dàng mở rộng để thêm các thông tin user khác (name, role, token...)
 */
public class SessionManager {
    private static final String PREF_NAME = "LaundryAppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_AUTH_TOKEN = "authToken";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    /**
     * Constructor
     * @param context Context của ứng dụng
     */
    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * Lưu phiên đăng nhập
     * @param email Email người dùng
     * @param userName Tên người dùng
     */
    public void createLoginSession(String email, String userName) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    /**
     * Lưu phiên đăng nhập với đầy đủ thông tin (dùng cho API)
     * @param email Email người dùng
     * @param userName Tên người dùng
     * @param userId ID người dùng
     * @param authToken Token xác thực
     */
    public void createLoginSession(String email, String userName, String userId, String authToken) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.apply();
    }

    /**
     * Kiểm tra trạng thái đăng nhập
     * @return true nếu đã đăng nhập, false nếu chưa
     */
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Lấy email người dùng
     * @return Email của người dùng đang đăng nhập
     */
    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, "");
    }

    /**
     * Lấy tên người dùng
     * @return Tên người dùng đang đăng nhập
     */
    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, "");
    }

    /**
     * Lấy ID người dùng
     * @return ID của người dùng đang đăng nhập
     */
    public String getUserId() {
        return preferences.getString(KEY_USER_ID, "");
    }

    /**
     * Lấy token xác thực
     * @return Token xác thực của người dùng
     */
    public String getAuthToken() {
        return preferences.getString(KEY_AUTH_TOKEN, "");
    }

    /**
     * Đăng xuất - Xóa tất cả thông tin phiên
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }

    /**
     * Cập nhật thông tin người dùng
     * @param userName Tên người dùng mới
     */
    public void updateUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }
}

