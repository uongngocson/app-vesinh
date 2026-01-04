# 🎨 Hướng Dẫn Thay Background Login Bằng Ảnh

## 📝 Cách Thực Hiện

### Bước 1: Chuẩn Bị Ảnh

1. Chọn ảnh bạn muốn dùng làm background
2. Resize ảnh về kích thước phù hợp (tối thiểu 1080x1920px cho full HD)
3. Đặt tên file: `login_background_image.jpg` hoặc `login_background_image.png`

### Bước 2: Thêm Ảnh Vào Project

1. Copy ảnh vào thư mục: `app/src/main/res/drawable/`
2. Hoặc kéo thả ảnh trực tiếp vào thư mục drawable trong Android Studio

### Bước 3: Build & Test

1. Sync project: **File** → **Sync Project with Gradle Files**
2. Build: **Build** → **Rebuild Project**
3. Run app để xem kết quả

## 🎨 Tùy Chỉnh Overlay

Trong file `bg_login_background.xml`, bạn có thể điều chỉnh độ mờ của overlay:

```xml
<!-- Overlay hoàn toàn đen với độ trong suốt 50% -->
<solid android:color="#80000000" />

<!-- Overlay tối hơn (60% opacity) -->
<solid android:color="#99000000" />

<!-- Overlay sáng hơn (30% opacity) -->
<solid android:color="#4D00000" />

<!-- Không overlay -->
<!-- Xóa item shape này -->
```

## 🔧 Các Tùy Chọn Khác

### 1. Scale Ảnh

```xml
<item android:drawable="@drawable/login_background_image"
      android:gravity="center"
      android:scaleType="centerCrop" />
```

### 2. Gradient Overlay

```xml
<item>
    <shape android:shape="rectangle">
        <gradient
            android:startColor="#80000000"
            android:endColor="#40000000"
            android:angle="90" />
    </shape>
</item>
```

### 3. Blur Effect (API 17+)

Sử dụng RenderScript để làm mờ ảnh background.

## ⚠️ Lưu Ý

1. **Kích thước ảnh**: Ảnh quá lớn có thể làm chậm app
2. **Định dạng**: PNG cho ảnh trong suốt, JPG cho ảnh chất lượng
3. **Tối ưu**: Nén ảnh trước khi thêm vào project
4. **Test**: Test trên nhiều thiết bị với độ phân giải khác nhau

## 🎯 Kết Quả Mong Đợi

- Background đẹp mắt với ảnh của bạn
- Text và UI elements vẫn rõ ràng nhờ overlay
- Performance tốt trên tất cả thiết bị
- Responsive trên nhiều màn hình

**Happy Coding! 🚀**
