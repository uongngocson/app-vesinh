package com.project.laundryappui.api;

import com.project.laundryappui.api.models.Address;
import com.project.laundryappui.api.models.ApiResponse;
import com.project.laundryappui.api.models.MessageResponse;
import com.project.laundryappui.api.models.UpdateProfileRequest;
import com.project.laundryappui.api.models.UserProfile;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * UsersApiService - Interface định nghĩa các API endpoints cho Users
 * Map với UsersController từ NestJS backend
 */
public interface UsersApiService {

    /**
     * GET /api/v1/users/profile
     * Lấy thông tin profile
     */
    @GET(ApiConfig.USERS_PROFILE)
    Call<ApiResponse<UserProfile>> getProfile();

    /**
     * PATCH /api/v1/users/profile
     * Cập nhật profile
     */
    @PATCH(ApiConfig.USERS_PROFILE)
    Call<ApiResponse<UserProfile>> updateProfile(@Body UpdateProfileRequest request);

    /**
     * GET /api/v1/users/addresses
     * Lấy danh sách địa chỉ
     */
    @GET(ApiConfig.USERS_ADDRESSES)
    Call<ApiResponse<List<Address>>> getAddresses();

    /**
     * POST /api/v1/users/addresses
     * Thêm địa chỉ mới
     */
    @POST(ApiConfig.USERS_ADDRESSES)
    Call<ApiResponse<Address>> addAddress(@Body Address address);

    /**
     * PATCH /api/v1/users/addresses/:id
     * Cập nhật địa chỉ
     */
    @PATCH("users/addresses/{id}")
    Call<ApiResponse<Address>> updateAddress(@Path("id") String addressId, @Body Address address);

    /**
     * DELETE /api/v1/users/addresses/:id
     * Xóa địa chỉ
     */
    @DELETE("users/addresses/{id}")
    Call<ApiResponse<MessageResponse>> deleteAddress(@Path("id") String addressId);

    /**
     * POST /api/v1/users/avatar
     * Upload avatar
     */
    @Multipart
    @POST(ApiConfig.USERS_AVATAR)
    Call<ApiResponse<UserProfile>> uploadAvatar(@Part MultipartBody.Part avatar);

    /**
     * DELETE /api/v1/users/avatar
     * Xóa avatar
     */
    @DELETE(ApiConfig.USERS_AVATAR)
    Call<ApiResponse<UserProfile>> deleteAvatar();
}

