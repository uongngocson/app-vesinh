package com.project.laundryappui.services.data;

import android.content.Context;

import com.google.gson.Gson;
import com.project.laundryappui.services.model.ServiceData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Data loader để đọc và parse JSON data từ assets
 * Chuẩn professional project - tách biệt data logic
 */
public class ServicesDataLoader {
    
    private static ServicesDataLoader instance;
    private ServiceData serviceData;
    
    private ServicesDataLoader() {
        // Private constructor for singleton
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized ServicesDataLoader getInstance() {
        if (instance == null) {
            instance = new ServicesDataLoader();
        }
        return instance;
    }
    
    /**
     * Load services data from JSON file trong assets
     * @param context Android context
     * @return ServiceData object hoặc null nếu có lỗi
     */
    public ServiceData loadServicesData(Context context) {
        // Cache data - chỉ load 1 lần
        if (serviceData != null) {
            return serviceData;
        }
        
        try {
            String jsonString = loadJSONFromAsset(context, "services_data.json");
            if (jsonString != null) {
                Gson gson = new Gson();
                serviceData = gson.fromJson(jsonString, ServiceData.class);
                return serviceData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Đọc file JSON từ assets folder
     * @param context Android context
     * @param fileName Tên file JSON
     * @return String content của file
     */
    private String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            reader.close();
            is.close();
            json = sb.toString();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return json;
    }
    
    /**
     * Clear cached data - useful khi cần reload
     */
    public void clearCache() {
        serviceData = null;
    }
    
    /**
     * Get cached service data
     * @return Cached ServiceData hoặc null
     */
    public ServiceData getCachedData() {
        return serviceData;
    }
}

