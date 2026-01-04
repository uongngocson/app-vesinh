package com.project.laundryappui.utils;

import android.content.Context;
import android.util.Log;

import com.project.laundryappui.model.Worker;

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
import java.util.Random;

/**
 * Quản lý đọc và parse dữ liệu thợ đánh giày từ JSON
 * Singleton pattern để tối ưu performance
 */
public class WorkerDataManager {
    private static final String TAG = "WorkerDataManager";
    private static final String WORKERS_JSON_FILE = "workers_data.json";
    
    private static WorkerDataManager instance;
    private Context context;
    
    // Cache dữ liệu để tránh đọc file nhiều lần
    private List<Worker> cachedWorkers;
    
    private WorkerDataManager(Context context) {
        this.context = context.getApplicationContext();
    }
    
    /**
     * Lấy instance singleton
     */
    public static synchronized WorkerDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new WorkerDataManager(context);
        }
        return instance;
    }
    
    /**
     * Lấy tất cả thợ từ JSON
     */
    public List<Worker> getAllWorkers() {
        if (cachedWorkers != null) {
            return new ArrayList<>(cachedWorkers);
        }
        
        cachedWorkers = loadWorkersFromJson();
        return new ArrayList<>(cachedWorkers);
    }
    
    /**
     * Lấy thợ ngẫu nhiên từ danh sách
     */
    public Worker getRandomWorker() {
        List<Worker> workers = getAllWorkers();
        if (workers.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return workers.get(random.nextInt(workers.size()));
    }
    
    /**
     * Tìm thợ phù hợp nhất dựa trên:
     * - Khoảng cách gần nhất
     * - Rating cao
     * - Chuyên về loại giày được yêu cầu
     * - Đã xác thực
     */
    public Worker findBestWorker(double customerLat, double customerLng, String serviceType) {
        List<Worker> workers = getAllWorkers();
        
        if (workers.isEmpty()) {
            return null;
        }
        
        // Lọc thợ có chuyên môn phù hợp
        List<Worker> suitableWorkers = new ArrayList<>();
        for (Worker worker : workers) {
            if (hasSpecialty(worker, serviceType)) {
                suitableWorkers.add(worker);
            }
        }
        
        // Nếu không có thợ chuyên về loại này, lấy tất cả
        if (suitableWorkers.isEmpty()) {
            suitableWorkers = new ArrayList<>(workers);
        }
        
        // Tính điểm cho mỗi thợ
        for (Worker worker : suitableWorkers) {
            double score = calculateWorkerScore(worker, customerLat, customerLng, serviceType);
            // Lưu điểm vào một field tạm (có thể thêm field score vào Worker model)
        }
        
        // Sắp xếp theo điểm (khoảng cách + rating)
        Collections.sort(suitableWorkers, new Comparator<Worker>() {
            @Override
            public int compare(Worker w1, Worker w2) {
                double score1 = calculateWorkerScore(w1, customerLat, customerLng, serviceType);
                double score2 = calculateWorkerScore(w2, customerLat, customerLng, serviceType);
                return Double.compare(score2, score1); // Sắp xếp giảm dần
            }
        });
        
        // Trả về thợ tốt nhất
        return suitableWorkers.get(0);
    }
    
    /**
     * Tính điểm cho thợ dựa trên nhiều yếu tố
     */
    private double calculateWorkerScore(Worker worker, double customerLat, double customerLng, String serviceType) {
        double score = 0;
        
        // Điểm từ rating (0-5 điểm)
        score += worker.getRating() * 10;
        
        // Điểm từ số đơn đã hoàn thành (tối đa 2 điểm)
        score += Math.min(worker.getCompletedOrders() / 100.0, 2.0);
        
        // Điểm từ khoảng cách (gần hơn = điểm cao hơn, tối đa 3 điểm)
        double distance = worker.calculateDistanceTo(customerLat, customerLng);
        if (distance < 1.0) {
            score += 3.0;
        } else if (distance < 2.0) {
            score += 2.0;
        } else if (distance < 5.0) {
            score += 1.0;
        }
        
        // Điểm từ chuyên môn (nếu có chuyên về loại giày này)
        if (hasSpecialty(worker, serviceType)) {
            score += 2.0;
        }
        
        // Điểm từ xác thực
        if (worker.isVerified()) {
            score += 1.0;
        }
        
        // Điểm từ thời gian phản hồi (nhanh hơn = điểm cao hơn)
        String responseTime = worker.getResponseTime();
        if (responseTime.contains("3")) {
            score += 1.5;
        } else if (responseTime.contains("4") || responseTime.contains("5")) {
            score += 1.0;
        } else if (responseTime.contains("6") || responseTime.contains("7")) {
            score += 0.5;
        }
        
        return score;
    }
    
    /**
     * Kiểm tra thợ có chuyên về loại giày này không
     */
    private boolean hasSpecialty(Worker worker, String serviceType) {
        String[] specialties = worker.getSpecialties();
        if (specialties == null) {
            return false;
        }
        
        for (String specialty : specialties) {
            if (specialty.equals(serviceType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Lấy thợ theo ID
     */
    public Worker getWorkerById(String workerId) {
        List<Worker> workers = getAllWorkers();
        
        for (Worker worker : workers) {
            if (worker.getId().equals(workerId)) {
                return worker;
            }
        }
        
        return null;
    }
    
    /**
     * Lọc thợ theo quận
     */
    public List<Worker> getWorkersByDistrict(String district) {
        List<Worker> allWorkers = getAllWorkers();
        List<Worker> filteredWorkers = new ArrayList<>();
        
        for (Worker worker : allWorkers) {
            if (worker.getDistrict() != null && worker.getDistrict().equals(district)) {
                filteredWorkers.add(worker);
            }
        }
        
        return filteredWorkers;
    }
    
    /**
     * Lọc thợ theo rating tối thiểu
     */
    public List<Worker> getWorkersByMinRating(double minRating) {
        List<Worker> allWorkers = getAllWorkers();
        List<Worker> filteredWorkers = new ArrayList<>();
        
        for (Worker worker : allWorkers) {
            if (worker.getRating() >= minRating) {
                filteredWorkers.add(worker);
            }
        }
        
        return filteredWorkers;
    }
    
    /**
     * Lấy thợ gần nhất dựa trên vị trí
     */
    public List<Worker> getWorkersSortedByDistance(double customerLat, double customerLng) {
        List<Worker> workers = getAllWorkers();
        
        // Tính khoảng cách cho mỗi thợ
        for (Worker worker : workers) {
            double distance = worker.calculateDistanceTo(customerLat, customerLng);
            // Distance đã được tính trong method calculateDistanceTo
        }
        
        // Sắp xếp theo khoảng cách tăng dần
        Collections.sort(workers, new Comparator<Worker>() {
            @Override
            public int compare(Worker w1, Worker w2) {
                double dist1 = w1.calculateDistanceTo(customerLat, customerLng);
                double dist2 = w2.calculateDistanceTo(customerLat, customerLng);
                return Double.compare(dist1, dist2);
            }
        });
        
        return workers;
    }
    
    /**
     * Đọc và parse JSON từ assets
     */
    private List<Worker> loadWorkersFromJson() {
        List<Worker> workers = new ArrayList<>();
        
        try {
            String jsonString = loadJsonFromAsset(WORKERS_JSON_FILE);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray workersArray = jsonObject.getJSONArray("workers");
            
            for (int i = 0; i < workersArray.length(); i++) {
                JSONObject workerObj = workersArray.getJSONObject(i);
                
                String id = workerObj.getString("id");
                String name = workerObj.getString("name");
                String phone = workerObj.getString("phone");
                double rating = workerObj.getDouble("rating");
                int completedOrders = workerObj.getInt("completedOrders");
                String responseTime = workerObj.getString("responseTime");
                String location = workerObj.getString("location");
                double latitude = workerObj.getDouble("latitude");
                double longitude = workerObj.getDouble("longitude");
                String district = workerObj.getString("district");
                String city = workerObj.getString("city");
                boolean isVerified = workerObj.getBoolean("isVerified");
                String avatar = workerObj.getString("avatar");
                
                // Parse specialties array
                JSONArray specialtiesArray = workerObj.getJSONArray("specialties");
                String[] specialties = new String[specialtiesArray.length()];
                for (int j = 0; j < specialtiesArray.length(); j++) {
                    specialties[j] = specialtiesArray.getString(j);
                }
                
                int experienceYears = workerObj.optInt("experienceYears", 0);
                
                Worker worker = new Worker(
                    id, name, phone, rating, completedOrders,
                    responseTime, location, latitude, longitude,
                    district, city, isVerified, avatar,
                    specialties, experienceYears
                );
                
                workers.add(worker);
            }
            
            Log.d(TAG, "Loaded " + workers.size() + " workers from JSON");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file", e);
        }
        
        return workers;
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
     * Clear cache khi cần refresh dữ liệu
     */
    public void clearCache() {
        cachedWorkers = null;
    }
}

