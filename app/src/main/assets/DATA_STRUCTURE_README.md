# Hướng Dẫn Sử Dụng Hệ Thống Dữ Liệu JSON

## 📁 Cấu Trúc Dữ Liệu

### File JSON: `laundry_stores.json`

File này chứa toàn bộ dữ liệu các tiệm giặt ủi tại TP. Hồ Chí Minh với 30+ cửa hàng phủ khắp các quận huyện.

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
      "services": ["wash", "iron", "dry_clean"]
    }
  ]
}
```

### Các Trường Dữ Liệu

- **id**: ID duy nhất của cửa hàng (int)
- **name**: Tên cửa hàng (String)
- **image**: Tên resource drawable (String - phải tương ứng với file trong res/drawable)
- **priceRange**: Khoảng giá dịch vụ (String)
- **address**: Địa chỉ chi tiết (String)
- **district**: Quận/Huyện (String)
- **distance**: Khoảng cách từ vị trí hiện tại (String)
- **rating**: Đánh giá từ 0-5 sao (double)
- **services**: Danh sách dịch vụ (Array String)

## 🔧 Sử Dụng JsonDataManager

### 1. Khởi Tạo

```java
JsonDataManager dataManager = JsonDataManager.getInstance(context);
```

### 2. Lấy Tất Cả Cửa Hàng

```java
List<HomeModel> allStores = dataManager.getAllStores();
```

### 3. Lọc Theo Quận/Huyện

```java
List<HomeModel> district1Stores = dataManager.getStoresByDistrict("Quận 1");
List<HomeModel> thuDucStores = dataManager.getStoresByDistrict("Thủ Đức");
```

### 4. Lọc Theo Rating Tối Thiểu

```java
// Chỉ lấy các cửa hàng có rating >= 4.5 sao
List<HomeModel> topRatedStores = dataManager.getStoresByMinRating(4.5);
```

### 5. Tìm Kiếm

```java
// Tìm theo tên hoặc quận
List<HomeModel> searchResults = dataManager.searchStores("Giặt Ủi");
List<HomeModel> searchResults2 = dataManager.searchStores("Thủ Đức");
```

### 6. Lấy Danh Sách Tất Cả Quận

```java
List<String> districts = dataManager.getAllDistricts();
```

### 7. Clear Cache (Khi Cần Refresh)

```java
dataManager.clearCache();
```

## 📊 Danh Sách Quận/Huyện Có Dữ Liệu

- Quận 1
- Quận 2 → Thủ Đức
- Quận 3
- Quận 4
- Quận 5
- Quận 6
- Quận 7
- Quận 8
- Quận 10
- Quận 11
- Quận 12
- Bình Thạnh
- Bình Tân
- Tân Bình
- Tân Phú
- Phú Nhuận
- Gò Vấp
- Thủ Đức
- Hóc Môn
- Củ Chi
- Nhà Bè
- Cần Giờ
- Bình Chánh

## 🎨 Thêm Dữ Liệu Mới

### Bước 1: Chuẩn bị ảnh

1. Thêm file ảnh vào `res/drawable/`
2. Đặt tên theo format: `bg_post1`, `bg_post2`, `store_image_1`, v.v.

### Bước 2: Thêm vào JSON

Mở file `assets/laundry_stores.json` và thêm object mới:

```json
{
  "id": 31,
  "name": "Tên Cửa Hàng Mới",
  "image": "bg_post_new",
  "priceRange": "$10-$25",
  "address": "Địa chỉ đầy đủ",
  "district": "Tên Quận",
  "distance": "2.5 km",
  "rating": 4.5,
  "services": ["wash", "iron"]
}
```

### Bước 3: Clear Cache (Nếu Cần)

```java
JsonDataManager.getInstance(context).clearCache();
```

## 💡 Best Practices

### 1. Singleton Pattern
JsonDataManager sử dụng Singleton để tránh tạo nhiều instance

### 2. Caching
Dữ liệu được cache sau lần đầu đọc để tăng performance

### 3. Fallback Data
Nếu JSON không load được, hệ thống tự động dùng dữ liệu mẫu

### 4. Error Handling
Tất cả các method đều có try-catch để handle lỗi gracefully

### 5. Thread-Safe
Sử dụng `synchronized` để đảm bảo thread-safe khi khởi tạo

## 🚀 Ví Dụ Sử Dụng Trong Fragment

```java
public class HomeFragment extends Fragment {
    private JsonDataManager jsonDataManager;
    private List<HomeModel> stores;
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Khởi tạo
        jsonDataManager = JsonDataManager.getInstance(requireContext());
        
        // Load dữ liệu
        loadStores();
    }
    
    private void loadStores() {
        // Tùy chọn 1: Lấy tất cả
        stores = jsonDataManager.getAllStores();
        
        // Tùy chọn 2: Lọc theo quận
        // stores = jsonDataManager.getStoresByDistrict("Quận 1");
        
        // Tùy chọn 3: Lọc theo rating
        // stores = jsonDataManager.getStoresByMinRating(4.5);
        
        // Cập nhật UI
        updateAdapter(stores);
    }
}
```

## 📝 Lưu Ý

1. **Đảm bảo tên drawable khớp**: Tên trong JSON phải match với file trong `res/drawable/`
2. **Rating hợp lệ**: Từ 0.0 đến 5.0
3. **Distance format**: Nên theo format "X.X km"
4. **Price format**: Nên theo format "$XX-$YY"
5. **District chuẩn hóa**: Sử dụng tên quận/huyện chính xác và nhất quán

## 🔄 Mở Rộng Trong Tương Lai

### Thêm API Backend

```java
public interface LaundryStoreApi {
    @GET("stores")
    Call<StoreResponse> getAllStores();
    
    @GET("stores/district/{district}")
    Call<StoreResponse> getStoresByDistrict(@Path("district") String district);
}
```

### Sync với Server

```java
public void syncWithServer() {
    // Fetch từ API
    // Update JSON local
    // Clear cache
    dataManager.clearCache();
}
```

### Thêm Location Service

```java
public List<HomeModel> getNearbyStores(double lat, double lng, double radius) {
    // Implement logic tính khoảng cách
    // Filter theo radius
    // Sort theo distance
}
```

## 📞 Support

Nếu có vấn đề với dữ liệu hoặc cần thêm tính năng, vui lòng báo cáo qua:
- GitHub Issues
- Email support team
- Documentation Wiki
