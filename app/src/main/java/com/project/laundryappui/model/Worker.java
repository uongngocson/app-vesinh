package com.project.laundryappui.model;

/**
 * Model class đại diện cho thông tin thợ đánh giày
 */
public class Worker {
    private String id;
    private String name;
    private String phone;
    private double rating;
    private int completedOrders;
    private String responseTime; // Thời gian phản hồi trung bình
    private String location; // Vị trí hiện tại (text)
    private double latitude;
    private double longitude;
    private String district; // Quận/Huyện
    private String city; // Thành phố
    private boolean isVerified; // Đã xác thực
    private String avatar; // Tên drawable resource
    private String[] specialties; // Các loại giày chuyên về
    private int experienceYears; // Số năm kinh nghiệm

    // Constructor mặc định
    public Worker() {
    }

    // Constructor đầy đủ
    public Worker(String id, String name, String phone, double rating, int completedOrders,
                  String responseTime, String location, double latitude, double longitude,
                  String district, String city, boolean isVerified, String avatar,
                  String[] specialties, int experienceYears) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.completedOrders = completedOrders;
        this.responseTime = responseTime;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.city = city;
        this.isVerified = isVerified;
        this.avatar = avatar;
        this.specialties = specialties;
        this.experienceYears = experienceYears;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String[] getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String[] specialties) {
        this.specialties = specialties;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    /**
     * Tính toán khoảng cách từ vị trí thợ đến vị trí khách hàng
     * @param customerLat Latitude của khách hàng
     * @param customerLng Longitude của khách hàng
     * @return Khoảng cách tính bằng km
     */
    public double calculateDistanceTo(double customerLat, double customerLng) {
        return calculateDistance(latitude, longitude, customerLat, customerLng);
    }

    /**
     * Tính khoảng cách giữa hai điểm trên bề mặt Trái Đất (Haversine formula)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính Trái Đất tính bằng km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Tính thời gian đến ước tính dựa trên khoảng cách
     * Giả sử tốc độ trung bình 30 km/h
     */
    public String getEstimatedArrivalTime(double customerLat, double customerLng) {
        double distance = calculateDistanceTo(customerLat, customerLng);
        int minutes = (int) Math.ceil((distance / 30.0) * 60); // 30 km/h
        
        if (minutes < 5) {
            return "5 phút";
        } else if (minutes < 10) {
            return "10 phút";
        } else if (minutes < 15) {
            return "15 phút";
        } else if (minutes < 20) {
            return "20 phút";
        } else if (minutes < 30) {
            return "30 phút";
        } else {
            return "Hơn 30 phút";
        }
    }

    /**
     * Format khoảng cách thành string
     */
    public String getFormattedDistance(double customerLat, double customerLng) {
        double distance = calculateDistanceTo(customerLat, customerLng);
        if (distance < 1.0) {
            return String.format("%.0f m", distance * 1000);
        } else {
            return String.format("%.1f km", distance);
        }
    }
}

