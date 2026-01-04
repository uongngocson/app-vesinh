package com.project.laundryappui.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnTokenCanceledListener;

/**
 * Quản lý vị trí người dùng và tính toán khoảng cách
 * Sử dụng Google Play Services Location API
 */
public class LocationManager {
    private static final String TAG = "LocationManager";
    private static LocationManager instance;
    
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    
    private static final double DEFAULT_LATITUDE = 10.7879;
    private static final double DEFAULT_LONGITUDE = 106.6984;
    
    
    private LocationManager(Context context) {
        this.context = context.getApplicationContext();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }
    
    public static synchronized LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager(context);
        }
        return instance;
    }
    
    /**
     * Kiểm tra quyền truy cập vị trí
     */
    public boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, 
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, 
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    
    /**
     * Lấy vị trí hiện tại (async)
     */
    public void getCurrentLocation(LocationCallback callback) {
        if (!hasLocationPermission()) {
            Log.w(TAG, "No location permission, using default location");
            callback.onLocationReceived(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            return;
        }
        
        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                new CancellationToken() {
                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                    
                    @Override
                    public CancellationToken onCanceledRequested(OnTokenCanceledListener listener) {
                        return this;
                    }
                }
            ).addOnSuccessListener(location -> {
                if (location != null) {
                    Log.d(TAG, "Got location: " + location.getLatitude() + ", " + location.getLongitude());
                    callback.onLocationReceived(location.getLatitude(), location.getLongitude());
                } else {
                    Log.w(TAG, "Location is null, using default");
                    callback.onLocationReceived(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
                }
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to get location", e);
                callback.onLocationReceived(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            });
        } catch (SecurityException e) {
            Log.e(TAG, "Security exception", e);
            callback.onLocationReceived(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }
    }
    
    /**
     * Lấy vị trí mặc định (sync) - Trung tâm Quận 1
     */
    public double[] getDefaultLocation() {
        return new double[]{DEFAULT_LATITUDE, DEFAULT_LONGITUDE};
    }
    
    /**
     * Tính khoảng cách giữa 2 điểm (theo công thức Haversine)
     * @return Khoảng cách tính bằng km
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính Trái Đất (km)
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
    
    /**
     * Format khoảng cách thành string
     */
    public static String formatDistance(double distanceKm) {
        if (distanceKm < 1) {
            return String.format("%.0f m", distanceKm * 1000);
        } else {
            return String.format("%.1f km", distanceKm);
        }
    }
    
    /**
     * Callback interface cho async location
     */
    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
    }
}

