package com.project.laundryappui.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.project.laundryappui.api.models.ApiError;
import com.project.laundryappui.api.models.ApiResponse;
import com.project.laundryappui.api.models.CleaningBookingRequest;
import com.project.laundryappui.api.models.Order;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * OrdersRepository - Repository pattern để xử lý logic API Orders
 * Đóng gói tất cả API calls và xử lý responses
 */
public class OrdersRepository {
    private static final String TAG = "OrdersRepository";

    private final Context context;
    private final OrdersApiService ordersApiService;

    public OrdersRepository(Context context) {
        this.context = context.getApplicationContext();
        this.ordersApiService = ApiClient.getInstance(context).create(OrdersApiService.class);
    }

    /**
     * Interface cho callbacks
     */
    public interface OrdersCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage);
    }

    /**
     * Tạo đơn hàng cleaning booking
     */
    public void createCleaningBooking(CleaningBookingRequest request, OrdersCallback<Order> callback) {
        ordersApiService.createCleaningBooking(request).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Order> apiResponse = response.body();

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
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                Log.e(TAG, "Create cleaning booking failed", t);
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
                return "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại thông tin.";
            case 401:
                return "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.";
            case 403:
                return "Bạn không có quyền thực hiện hành động này.";
            case 404:
                return "Không tìm thấy tài nguyên yêu cầu.";
            case 409:
                return "Đơn hàng đã tồn tại hoặc có xung đột dữ liệu.";
            case 422:
                return "Dữ liệu đầu vào không hợp lệ.";
            case 500:
                return "Lỗi máy chủ. Vui lòng thử lại sau.";
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
