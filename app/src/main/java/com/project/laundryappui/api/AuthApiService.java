package com.project.laundryappui.api;

import com.project.laundryappui.api.models.ApiResponse;
import com.project.laundryappui.api.models.AuthResponse;
import com.project.laundryappui.api.models.ChangePasswordRequest;
import com.project.laundryappui.api.models.LoginRequest;
import com.project.laundryappui.api.models.MessageResponse;
import com.project.laundryappui.api.models.RegisterRequest;
import com.project.laundryappui.api.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * AuthApiService - Interface định nghĩa các API endpoints cho Auth
 * Map với AuthController từ NestJS backend
 */
public interface AuthApiService {

    /**
     * POST /api/v1/auth/login
     * Đăng nhập
     */
    @POST(ApiConfig.AUTH_LOGIN)
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    /**
     * POST /api/v1/auth/register
     * Đăng ký tài khoản mới
     */
    @POST(ApiConfig.AUTH_REGISTER)
    Call<ApiResponse<AuthResponse>> register(@Body RegisterRequest request);

    /**
     * POST /api/v1/auth/logout
     * Đăng xuất
     */
    @POST(ApiConfig.AUTH_LOGOUT)
    Call<ApiResponse<MessageResponse>> logout();

    /**
     * POST /api/v1/auth/refresh
     * Refresh access token
     */
    @POST(ApiConfig.AUTH_REFRESH)
    Call<ApiResponse<AuthResponse>> refreshToken();

    /**
     * GET /api/v1/auth/me
     * Lấy thông tin user hiện tại
     */
    @GET(ApiConfig.AUTH_ME)
    Call<ApiResponse<User>> getMe();

    /**
     * POST /api/v1/auth/change-password
     * Đổi mật khẩu
     */
    @POST(ApiConfig.AUTH_CHANGE_PASSWORD)
    Call<ApiResponse<MessageResponse>> changePassword(@Body ChangePasswordRequest request);
}

