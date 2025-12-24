package com.project.laundryappui.api;

import android.content.Context;

import com.project.laundryappui.utils.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AuthInterceptor - Tự động thêm Authorization header vào mọi request
 */
public class AuthInterceptor implements Interceptor {
    private final TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Nếu không có token, gửi request bình thường
        if (!tokenManager.hasToken()) {
            return chain.proceed(originalRequest);
        }

        // Thêm Authorization header
        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", tokenManager.getAuthorizationHeader())
                .build();

        return chain.proceed(authenticatedRequest);
    }
}

