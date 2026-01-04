package com.project.laundryappui.menu.home.model;

/**
 * Model đại diện cho một tiệm giặt ủi
 * Hỗ trợ đầy đủ thông tin từ JSON data
 */
public class HomeModel {
    private int id;
    private int image;
    private String name;
    private String price;
    private String location;
    private String address;
    private String district;
    private double rating;
    private double latitude;
    private double longitude;
    private String phone;
    private double distanceFromUser; // Khoảng cách tính từ vị trí user (km)

    public HomeModel() {}

    // Constructor cũ để tương thích ngược
    public HomeModel(int image, String name, String price, String location) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.location = location;
        this.rating = 0.0;
    }

    // Constructor đầy đủ cho JSON data
    public HomeModel(int id, int image, String name, String price, 
                     String address, String district, String location, double rating,
                     double latitude, double longitude, String phone) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.address = address;
        this.district = district;
        this.location = location;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.distanceFromUser = 0.0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Alias cho getName để dễ sử dụng
    public String getTitle() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    @Override
    public String toString() {
        return "HomeModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", district='" + district + '\'' +
                ", rating=" + rating +
                ", distanceFromUser=" + distanceFromUser +
                '}';
    }
}
