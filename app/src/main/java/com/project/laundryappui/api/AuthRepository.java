package com.project.laundryappui.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.project.laundryappui.api.models.ApiError;
import com.project.laundryappui.api.models.ApiResponse;
import com.project.laundryappui.api.models.AuthResponse;
import com.project.laundryappui.api.models.ChangePasswordRequest;
import com.project.laundryappui.api.models.LoginRequest;
import com.project.laundryappui.api.models.MessageResponse;
import com.project.laundryappui.api.models.RegisterRequest;
import com.project.laundryappui.api.models.User;
import com.project.laundryappui.utils.SessionManager;
import com.project.laundryappui.utils.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * AuthRepository - Repository pattern để xử lý logic API
 * Đóng gói tất cả API calls và xử lý responses
 */
public class AuthRepository {
    private static final String TAG = "AuthRepository";
    
    private final Context context;
    private final AuthApiService authApiService;
    private final TokenManager tokenManager;
    private final SessionManager sessionManager;

    public AuthRepository(Context context) {
        this.context = context.getApplicationContext();
        this.authApiService = ApiClient.getInstance(context).create(AuthApiService.class);
        this.tokenManager = new TokenManager(context);
        this.sessionManager = new SessionManager(context);
    }

    /**
     * Interface cho callbacks
     */
    public interface AuthCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage);
    }

    /**
     * Đăng nhập
     */
    public void login(String email, String password, AuthCallback<AuthResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);
        
        authApiService.login(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        AuthResponse authResponse = apiResponse.getData();
                        
                        // Lưu token
                        tokenManager.saveAccessToken(
                                authResponse.getAccessToken(),
                                authResponse.getTokenType(),
                                authResponse.getExpiresIn()
                        );
                        
                        // Lưu session
                        User user = authResponse.getUser();
                        if (user != null) {
                            sessionManager.createLoginSession(
                                    user.getEmail(),
                                    user.getFullName(),
                                    user.getId(),
                                    authResponse.getAccessToken()
                            );
                        }
                        
                        callback.onSuccess(authResponse);
                    } else {
                        callback.onError("Không nhận được dữ liệu từ máy chủ");
                    }
                } else {
                    String errorMsg = parseErrorResponse(response);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                Log.e(TAG, "Login failed", t);
                callback.onError(getErrorMessage(t));
            }
        });
    }

    /**
     * Đăng ký
     */
    public void register(String email, String phone, String password, String fullName, 
                        AuthCallback<AuthResponse> callback) {
        RegisterRequest request = new RegisterRequest(email, phone, password, fullName);
        
        authApiService.register(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        AuthResponse authResponse = apiResponse.getData();
                        
                        // Lưu token
                        tokenManager.saveAccessToken(
                                authResponse.getAccessToken(),
                                authResponse.getTokenType(),
                                authResponse.getExpiresIn()
                        );
                        
                        // Lưu session
                        User user = authResponse.getUser();
                        if (user != null) {
                            sessionManager.createLoginSession(
                                    user.getEmail(),
                                    user.getFullName(),
                                    user.getId(),
                                    authResponse.getAccessToken()
                            );
                        }
                        
                        callback.onSuccess(authResponse);
                    } else {
                        callback.onError("Không nhận được dữ liệu từ máy chủ");
                    }
                } else {
                    String errorMsg = parseErrorResponse(response);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                Log.e(TAG, "Register failed", t);
                callback.onError(getErrorMessage(t));
            }
        });
    }

    /**
     * Đăng xuất
     */
    public void logout(AuthCallback<String> callback) {
        authApiService.logout().enqueue(new Callback<ApiResponse<MessageResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MessageResponse>> call, Response<ApiResponse<MessageResponse>> response) {
                // Dù API có thành công hay không, vẫn clear local data
                clearLocalData();
                
                if (response.isSuccessful() && response.body() != null 
                    && response.body().getData() != null) {
                    callback.onSuccess(response.body().getData().getMessage());
                } else {
                    callback.onSuccess("Đăng xuất thành công");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MessageResponse>> call, Throwable t) {
                // Vẫn clear local data nếu API fail
                clearLocalData();
                callback.onSuccess("Đăng xuất thành công");
            }
        });
    }

    /**
     * Đổi mật khẩu
     */
    public void changePassword(String currentPassword, String newPassword, String confirmPassword,
                              AuthCallback<String> callback) {
        ChangePasswordRequest request = new ChangePasswordRequest(
                currentPassword, newPassword, confirmPassword);
        
        authApiService.changePassword(request).enqueue(new Callback<ApiResponse<MessageResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<MessageResponse>> call, Response<ApiResponse<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null 
                    && response.body().getData() != null) {
                    // Backend sẽ xóa tất cả sessions, nên cần clear local data
                    clearLocalData();
                    callback.onSuccess(response.body().getData().getMessage());
                } else {
                    String errorMsg = parseErrorResponse(response);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MessageResponse>> call, Throwable t) {
                Log.e(TAG, "Change password failed", t);
                callback.onError(getErrorMessage(t));
            }
        });
    }

    /**
     * Quên mật khẩu - Gửi email reset
     * TODO: Backend cần implement endpoint POST /auth/forgot-password
     * @param email Email người dùng
     */
    public void forgotPassword(String email, AuthCallback<String> callback) {
        // TODO: Implement khi backend có API
        // ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        // authApiService.forgotPassword(request).enqueue(...);
        
        // Demo: Giả lập thành công
        callback.onSuccess("Link đặt lại mật khẩu đã được gửi đến email của bạn");
    }

    /**
     * Lấy thông tin user hiện tại
     */
    public void getMe(AuthCallback<User> callback) {
        authApiService.getMe().enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null 
                    && response.body().getData() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String errorMsg = parseErrorResponse(response);
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Log.e(TAG, "Get me failed", t);
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
                return "Dữ liệu đã tồn tại";
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

    /**
     * Clear tất cả local data
     */
    private void clearLocalData() {
        tokenManager.clearTokens();
        sessionManager.logout();
        ApiClient.resetInstance();
    }
}

