# Hệ Thống Đề Xuất Tiệm Giặt Ủi Theo Vị Trí

## 📍 Tổng Quan

Hệ thống location-based recommendation cho phép:
- Tự động lấy vị trí hiện tại của người dùng
- Tính khoảng cách đến các tiệm giặt ủi
- Sắp xếp và hiển thị tiệm gần nhất lên đầu
- Hiển thị chi tiết cửa hàng với dữ liệu từ JSON

## 🏗️ Kiến Trúc

### 1. LocationManager (`utils/LocationManager.java`)

**Chức năng:**
- Lấy vị trí hiện tại của user sử dụng Google Play Services
- Tính khoảng cách giữa 2 điểm địa lý (công thức Haversine)
- Format khoảng cách thành string dễ đọc

**Sử dụng:**
```java
LocationManager locationManager = LocationManager.getInstance(context);

// Lấy vị trí async
locationManager.getCurrentLocation((latitude, longitude) -> {
    Log.d(TAG, "User location: " + latitude + ", " + longitude);
    // Xử lý với vị trí
});

// Tính khoảng cách
double distance = LocationManager.calculateDistance(
    userLat, userLng, 
    storeLat, storeLng
);

// Format khoảng cách
String formatted = LocationManager.formatDistance(distance); // "2.5 km" hoặc "500 m"
```

### 2. JsonDataManager (`utils/JsonDataManager.java`)

**Chức năng mới:**
- Parse latitude, longitude, phone từ JSON
- Sort cửa hàng theo khoảng cách từ user
- Cập nhật distanceFromUser cho mỗi cửa hàng

**Methods quan trọng:**
```java
JsonDataManager manager = JsonDataManager.getInstance(context);

// Lấy và sort theo khoảng cách
List<HomeModel> stores = manager.getStoresSortedByDistance(userLat, userLng);

// Lấy top N cửa hàng gần nhất
List<HomeModel> nearest = manager.getNearestStores(userLat, userLng, 10);

// Tìm cửa hàng theo ID
HomeModel store = manager.getStoreById(5);
```

### 3. HomeModel (`menu/home/model/HomeModel.java`)

**Fields mới:**
- `latitude`: Vĩ độ của cửa hàng
- `longitude`: Kinh độ của cửa hàng
- `phone`: Số điện thoại
- `distanceFromUser`: Khoảng cách tính toán từ user (km)

### 4. HomeFragment (`menu/home/HomeFragment.java`)

**Flow:**
1. Khởi tạo LocationManager và JsonDataManager
2. Gọi `loadStoresWithLocation()`
3. Lấy vị trí user (async)
4. Load và sort cửa hàng theo khoảng cách
5. Update adapter trên UI thread

### 5. HomeDetailActivity (`menu/home/home_detail/HomeDetailActivity.java`)

**Chức năng:**
- Nhận `STORE_ID` từ Intent
- Load dữ liệu từ JSON theo ID
- Hiển thị thông tin đầy đủ
- Xử lý actions: gọi điện, chat, đặt lịch

**Sử dụng:**
```java
Intent intent = new Intent(context, HomeDetailActivity.class);
intent.putExtra("STORE_ID", store.getId());
startActivity(intent);
```

### 6. HomeAdapter (`menu/home/adapter/HomeAdapter.java`)

**Cập nhật:**
- Click listener pass STORE_ID sang HomeDetailActivity
- Hiển thị khoảng cách thực tế

## 📱 Permissions

**AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

**Runtime Permission:**
App cần request location permission lúc runtime (Android 6.0+):
```java
if (ContextCompat.checkSelfPermission(this, 
    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
}
```

## 📊 Dữ Liệu JSON

**Cấu trúc (laundry_stores.json):**
```json
{
  "stores": [
    {
      "id": 1,
      "name": "Giặt Ủi Hoàng Gia Quận 1",
      "image": "bg_post1",
      "priceRange": "$15-$30",
      "address": "123 Nguyễn Huệ, Phường Bến Nghé, Quận 1",
      "district": "Quận 1",
      "distance": "1.2 km",
      "rating": 4.8,
      "latitude": 10.7756,
      "longitude": 106.7019,
      "phone": "028 3829 5555",
      "services": ["wash", "iron", "dry_clean"]
    }
  ]
}
```

**Coordinates của TP.HCM:**
- Trung tâm Quận 1: `10.7756, 106.7019`
- Thủ Đức: `10.8506, 106.7719`
- Quận 7: `10.7311, 106.7218`
- v.v.

## 🔄 Flow Hoàn Chỉnh

```
User mở HomeFragment
    ↓
Lấy vị trí user (LocationManager)
    ↓
Load JSON stores (JsonDataManager)
    ↓
Tính khoảng cách cho từng store
    ↓
Sort theo khoảng cách tăng dần
    ↓
Update distanceFromUser & location string
    ↓
Hiển thị RecyclerView (gần nhất ở đầu)
    ↓
User click vào store
    ↓
Pass STORE_ID qua Intent
    ↓
HomeDetailActivity load store theo ID
    ↓
Hiển thị chi tiết đầy đủ
```

## 🎯 Tính Năng Chính

### 1. Auto-detect Location
- Tự động lấy GPS nếu có permission
- Fallback về vị trí mặc định (Quận 1) nếu không có GPS

### 2. Real-time Distance
- Tính toán khoảng cách thực tế (km)
- Format hiển thị: "2.5 km" hoặc "500 m"
- Cập nhật location string của mỗi store

### 3. Smart Sorting
- Sắp xếp tự động theo khoảng cách
- Store gần nhất hiển thị đầu tiên
- Giúp user dễ dàng tìm tiệm gần nhất

### 4. Detailed View
- Hiển thị tất cả thông tin từ JSON
- Button gọi điện thoại (Intent to Dialer)
- Button chat (chờ implement)
- Button đặt lịch (chờ implement)

## 🛠️ Dependencies

**build.gradle (app):**
```gradle
// Google Play Services - Location
implementation 'com.google.android.gms:play-services-location:21.0.1'
```

## 📝 Testing

### Test Location
1. Emulator: Use Extended Controls → Location để set GPS
2. Real Device: Bật GPS và cấp permission

### Test Distance Calculation
```java
// Quận 1 → Thủ Đức
double dist = LocationManager.calculateDistance(
    10.7756, 106.7019,  // Quận 1
    10.8506, 106.7719   // Thủ Đức
);
// Expected: ~12 km
```

### Test Data Flow
1. Check log: "Got user location: ..."
2. Check log: "Loaded and sorted X stores by distance"
3. Verify RecyclerView hiển thị store gần nhất đầu tiên
4. Click vào store, verify HomeDetailActivity hiển thị đúng data

## 🔧 Troubleshooting

### Location không hoạt động
- Kiểm tra permission trong Settings
- Bật GPS/Location Services
- Kiểm tra Google Play Services

### Distance không chính xác
- Verify coordinates trong JSON
- Check công thức Haversine
- Verify Earth radius (6371 km)

### Store detail không hiển thị
- Check STORE_ID có được pass qua Intent không
- Verify store tồn tại trong JSON
- Check logs: "Store not found with ID: X"

## 🚀 Mở Rộng Tương Lai

### 1. Filter by Distance
```java
List<HomeModel> nearbyStores = stores.stream()
    .filter(s -> s.getDistanceFromUser() <= 5.0) // trong bán kính 5km
    .collect(Collectors.toList());
```

### 2. Map View
- Integrate Google Maps
- Hiển thị markers cho các store
- Show route từ user đến store

### 3. Favorites
- Lưu danh sách store yêu thích
- Quick access không cần location

### 4. Notifications
- Thông báo khi user gần store yêu thích
- Geofencing cho promotions

## 📞 Contact Actions

### Call Button
```java
Intent intent = new Intent(Intent.ACTION_DIAL);
intent.setData(Uri.parse("tel:" + store.getPhone()));
startActivity(intent);
```

### Map Navigation
```java
String uri = "google.navigation:q=" + store.getLatitude() + "," + store.getLongitude();
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
intent.setPackage("com.google.android.apps.maps");
startActivity(intent);
```

## ✅ Checklist Implementation

- [x] LocationManager utility
- [x] JsonDataManager với distance sorting
- [x] HomeModel với location fields
- [x] JSON data với coordinates (30 stores)
- [x] HomeFragment load và sort theo location
- [x] HomeAdapter với click listener
- [x] HomeDetailActivity hiển thị chi tiết
- [x] AndroidManifest permissions
- [x] build.gradle dependencies
- [ ] Runtime permission request
- [ ] Map view integration
- [ ] Chat functionality
- [ ] Appointment booking

## 📚 Resources

- [Haversine Formula](https://en.wikipedia.org/wiki/Haversine_formula)
- [Google Play Services Location](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)
- [FusedLocationProviderClient](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient)

---

**Version:** 1.0  
**Last Updated:** December 2025  
**Author:** AI Assistant

