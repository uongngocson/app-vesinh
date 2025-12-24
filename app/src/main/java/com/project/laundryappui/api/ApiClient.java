package com.project.laundryappui.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient - Singleton client cho Retrofit
 * Tạo và cấu hình Retrofit instance
 */
public class ApiClient {
    private static ApiClient instance;
    private final Retrofit retrofit;

    private ApiClient(Context context) {
        // Logging Interceptor (chỉ dùng cho development)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Auth Interceptor
        AuthInterceptor authInterceptor = new AuthInterceptor(context);

        // OkHttp Client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL + ApiConfig.API_PREFIX)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Lấy instance singleton
     */
    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Tạo service instance
     */
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * Reset instance (dùng khi logout)
     */
    public static void resetInstance() {
        instance = null;
    }
}

