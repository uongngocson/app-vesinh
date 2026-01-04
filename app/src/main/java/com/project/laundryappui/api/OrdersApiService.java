package com.project.laundryappui.api;

import com.project.laundryappui.api.models.ApiResponse;
import com.project.laundryappui.api.models.CleaningBookingRequest;
import com.project.laundryappui.api.models.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * OrdersApiService - Interface định nghĩa các API endpoints cho Orders
 * Map với OrdersController từ NestJS backend
 */
public interface OrdersApiService {

    /**
     * POST /api/v1/orders/cleaning-booking
     * Tạo đơn hàng book vệ sinh giày/túi xách
     */
    @POST(ApiConfig.ORDERS_CLEANING_BOOKING)
    Call<ApiResponse<Order>> createCleaningBooking(@Body CleaningBookingRequest request);
}
