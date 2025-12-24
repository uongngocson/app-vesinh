package com.project.laundryappui.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.project.laundryappui.api.models.ApiError;
import com.project.laundryappui.api.models.ApiResponse;
import com.project.laundryappui.api.models.UpdateProfileRequest;
import com.project.laundryappui.api.models.UserProfile;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UsersRepository - Repository pattern để xử lý logic API Users
 * Đóng gói tất cả API calls và xử lý responses
 */
public class UsersRepository {
    private static final String TAG = "UsersRepository";
    
    private final Context context;
    private final UsersApiService usersApiService;

    public UsersRepository(Context context) {
        this.context = context.getApplicationContext();
        this.usersApiService = ApiClient.getInstance(context).create(UsersApiService.class);
    }

    /**
     * Interface cho callbacks
     */
    public interface UsersCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage);
    }

    /**
     * Lấy thông tin profile
     */
    public void getProfile(UsersCallback<UserProfile> callback) {
        usersApiService.getProfile().enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserProfile> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError("Không nhận được dữ liệu từ máy chủ");
                    }
                } else {
                    String errorMsg = parseErrorResponse(response);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                Log.e(TAG, "Get profile failed", t);
                callback.onError(getErrorMessage(t));
            }
        });
    }

    /**
     * Cập nhật profile
     */
    public void updateProfile(UpdateProfileRequest request, UsersCallback<UserProfile> callback) {
        usersApiService.updateProfile(request).enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserProfile> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError("Không nhận được dữ liệu từ máy chủ");
                    }
                } else {
                    String errorMsg = parseErrorResponse(response);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                Log.e(TAG, "Update profile failed", t);
                callback.onError(getErrorMessage(t));
            }
        });
    }

    /**
     * Parse error response từ API
     */
    private String parseErrorResponse(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();
                Gson gson = new Gson();
                ApiError apiError = gson.fromJson(errorJson, ApiError.class);
                return apiError.getDisplayMessage();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error parsing error response", e);
        }
        
        // Fallback error messages
        switch (response.code()) {
            case 400:
                return "Dữ liệu không hợp lệ";
            case 401:
                return "Không có quyền truy cập";
            case 404:
                return "Không tìm thấy";
            case 409:
                return "Số điện thoại đã được sử dụng";
            case 500:
                return "Lỗi máy chủ";
            default:
                return "Có lỗi xảy ra. Vui lòng thử lại.";
        }
    }

    /**
     * Lấy error message từ Throwable
     */
    private String getErrorMessage(Throwable t) {
        if (t instanceof IOException) {
            return "Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng.";
        }
        return "Có lỗi xảy ra: " + t.getMessage();
    }
}

