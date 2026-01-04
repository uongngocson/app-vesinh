package com.project.laundryappui.utils;

import android.content.Context;
import android.util.Log;

import com.project.laundryappui.menu.home.model.HomeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Quản lý đọc và parse dữ liệu JSON từ assets
 * Singleton pattern để tối ưu performance
 */
public class JsonDataManager {
    private static final String TAG = "JsonDataManager";
    private static JsonDataManager instance;
    private Context context;
    
    // Cache dữ liệu để tránh đọc file nhiều lần
    private List<HomeModel> cachedStores;
    
    private JsonDataManager(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Lấy instance singleton
     */
    public static synchronized JsonDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new JsonDataManager(context);
        }
        return instance;
    }
    
    /**
     * Đọc tất cả cửa hàng từ JSON
     */
    public List<HomeModel> getAllStores() {
        if (cachedStores != null) {
            return new ArrayList<>(cachedStores);
        }
        
        cachedStores = loadStoresFromJson();
        return new ArrayList<>(cachedStores);
    }
    
    /**
     * Lọc cửa hàng theo quận
     */
    public List<HomeModel> getStoresByDistrict(String district) {
        List<HomeModel> allStores = getAllStores();
        List<HomeModel> filteredStores = new ArrayList<>();
        
        for (HomeModel store : allStores) {
            if (store.getDistrict() != null && store.getDistrict().equals(district)) {
                filteredStores.add(store);
            }
        }
        
        return filteredStores;
    }
    
    /**
     * Tìm kiếm cửa hàng theo tên
     */
    public List<HomeModel> searchStores(String query) {
        List<HomeModel> allStores = getAllStores();
        List<HomeModel> searchResults = new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        for (HomeModel store : allStores) {
            if (store.getTitle().toLowerCase().contains(lowerQuery) ||
                store.getDistrict().toLowerCase().contains(lowerQuery)) {
                searchResults.add(store);
            }
        }
        
        return searchResults;
    }
    
    /**
     * Lấy cửa hàng theo rating tối thiểu
     */
    public List<HomeModel> getStoresByMinRating(double minRating) {
        List<HomeModel> allStores = getAllStores();
        List<HomeModel> filteredStores = new ArrayList<>();
        
        for (HomeModel store : allStores) {
            if (store.getRating() >= minRating) {
                filteredStores.add(store);
            }
        }
        
        return filteredStores;
    }
    
    /**
     * Đọc và parse JSON từ assets
     */
    private List<HomeModel> loadStoresFromJson() {
        List<HomeModel> stores = new ArrayList<>();
        
        try {
            String jsonString = loadJsonFromAsset("laundry_stores.json");
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray storesArray = jsonObject.getJSONArray("stores");
            
            for (int i = 0; i < storesArray.length(); i++) {
                JSONObject storeObj = storesArray.getJSONObject(i);
                
                int id = storeObj.getInt("id");
                String name = storeObj.getString("name");
                String imageName = storeObj.getString("image");
                String priceRange = storeObj.getString("priceRange");
                String address = storeObj.getString("address");
                String district = storeObj.getString("district");
                String distance = storeObj.getString("distance");
                double rating = storeObj.getDouble("rating");
                double latitude = storeObj.optDouble("latitude", 10.7756);
                double longitude = storeObj.optDouble("longitude", 106.7019);
                String phone = storeObj.optString("phone", "N/A");
                
                // Chuyển đổi tên image thành resource ID
                int imageResId = getImageResourceId(imageName);
                
                HomeModel store = new HomeModel(
                    id,
                    imageResId,
                    name,
                    priceRange,
                    address,
                    district,
                    distance,
                    rating,
                    latitude,
                    longitude,
                    phone
                );
                
                stores.add(store);
            }
            
            Log.d(TAG, "Loaded " + stores.size() + " stores from JSON");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file", e);
        }
        
        return stores;
    }
    
    /**
     * Đọc file JSON từ assets
     */
    private String loadJsonFromAsset(String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }
    
    /**
     * Chuyển đổi tên image thành resource ID
     */
    private int getImageResourceId(String imageName) {
        try {
            return context.getResources().getIdentifier(
                imageName, 
                "drawable", 
                context.getPackageName()
            );
        } catch (Exception e) {
            Log.e(TAG, "Error getting image resource for: " + imageName, e);
            return 0;
        }
    }
    
    /**
     * Clear cache khi cần refresh dữ liệu
     */
    public void clearCache() {
        cachedStores = null;
    }
    
    /**
     * Lấy danh sách tất cả các quận
     */
    public List<String> getAllDistricts() {
        List<HomeModel> allStores = getAllStores();
        List<String> districts = new ArrayList<>();
        
        for (HomeModel store : allStores) {
            if (!districts.contains(store.getDistrict())) {
                districts.add(store.getDistrict());
            }
        }
        
        return districts;
    }
    
    /**
     * Lấy cửa hàng gần nhất dựa trên vị trí user
     * @param userLat Latitude của user
     * @param userLng Longitude của user
     * @return Danh sách cửa hàng đã sắp xếp theo khoảng cách
     */
    public List<HomeModel> getStoresSortedByDistance(double userLat, double userLng) {
        List<HomeModel> stores = getAllStores();
        
        // Tính khoảng cách cho mỗi cửa hàng
        for (HomeModel store : stores) {
            double distance = LocationManager.calculateDistance(
                userLat, 
                userLng, 
                store.getLatitude(), 
                store.getLongitude()
            );
            store.setDistanceFromUser(distance);
            
            // Cập nhật location string với khoảng cách thực tế
            store.setLocation(LocationManager.formatDistance(distance));
        }
        
        // Sắp xếp theo khoảng cách tăng dần
        Collections.sort(stores, new Comparator<HomeModel>() {
            @Override
            public int compare(HomeModel s1, HomeModel s2) {
                return Double.compare(s1.getDistanceFromUser(), s2.getDistanceFromUser());
            }
        });
        
        Log.d(TAG, "Sorted " + stores.size() + " stores by distance from user");
        return stores;
    }
    
    /**
     * Lấy top N cửa hàng gần nhất
     */
    public List<HomeModel> getNearestStores(double userLat, double userLng, int limit) {
        List<HomeModel> sortedStores = getStoresSortedByDistance(userLat, userLng);
        
        if (sortedStores.size() <= limit) {
            return sortedStores;
        }
        
        return sortedStores.subList(0, limit);
    }
    
    /**
     * Tìm cửa hàng theo ID
     */
    public HomeModel getStoreById(int id) {
        List<HomeModel> allStores = getAllStores();
        
        for (HomeModel store : allStores) {
            if (store.getId() == id) {
                return store;
            }
        }
        
        return null;
    }
}

