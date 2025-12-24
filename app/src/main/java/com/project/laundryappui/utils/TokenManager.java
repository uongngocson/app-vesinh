package com.project.laundryappui.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * TokenManager - Quản lý JWT tokens
 * Lưu trữ accessToken và refreshToken (trong cookie - xử lý tự động)
 */
public class TokenManager {
    private static final String PREF_NAME = "LaundryTokens";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_TOKEN_TYPE = "tokenType";
    private static final String KEY_EXPIRES_IN = "expiresIn";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public TokenManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * Lưu access token
     */
    public void saveAccessToken(String accessToken, String tokenType, String expiresIn) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        editor.putString(KEY_EXPIRES_IN, expiresIn);
        editor.apply();
    }

    /**
     * Lấy access token
     */
    public String getAccessToken() {
        return preferences.getString(KEY_ACCESS_TOKEN, "");
    }

    /**
     * Lấy token type (Bearer)
     */
    public String getTokenType() {
        return preferences.getString(KEY_TOKEN_TYPE, "Bearer");
    }

    /**
     * Lấy authorization header đầy đủ
     * @return "Bearer {token}"
     */
    public String getAuthorizationHeader() {
        String token = getAccessToken();
        if (token.isEmpty()) {
            return "";
        }
        return getTokenType() + " " + token;
    }

    /**
     * Kiểm tra có token hay không
     */
    public boolean hasToken() {
        return !getAccessToken().isEmpty();
    }

    /**
     * Xóa tất cả tokens
     */
    public void clearTokens() {
        editor.clear();
        editor.apply();
    }
}

