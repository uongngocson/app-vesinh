# Service Detail Module

## Tổng quan
Module chi tiết dịch vụ giặt ủi với UI chuyên nghiệp, hiển thị đầy đủ thông tin giá cả cho từng loại đồ.

## Cấu trúc

### 1. ServiceDetailActivity
**Path:** `services/ServiceDetailActivity.java`

**Chức năng:**
- Hiển thị thông tin chi tiết dịch vụ
- Danh sách giá theo từng loại đồ
- Tính tổng và đặt hàng

**Service Types:**
- `IRONING` - Dịch vụ ủi đồ
- `WASH_IRON` - Dịch vụ giặt và ủi
- `DRY_CLEAN` - Dịch vụ giặt khô
- `MORE` - Dịch vụ khác

### 2. ServicePriceAdapter
**Path:** `services/adapter/ServicePriceAdapter.java`

**Chức năng:**
- Adapter cho RecyclerView hiển thị danh sách giá
- Handle click events cho từng item
- Format giá tiền theo locale Việt Nam

### 3. ServiceItem Model
**Path:** `services/model/ServiceItem.java`

**Properties:**
- `name`: Tên món đồ
- `price`: Giá (VND)
- `estimatedTime`: Thời gian ước tính
- `iconResource`: Icon resource ID
- `quantity`: Số lượng

## Layouts

### 1. activity_service_detail.xml
**Sections:**
- Header với icon và tên dịch vụ
- Service features (3 đặc điểm nổi bật)
- Estimated time card
- Price list với RecyclerView
- Bottom action bar (Total + Book Now button)

### 2. item_service_price.xml
**Components:**
- Icon món đồ
- Tên món đồ
- Thời gian ước tính
- Giá tiền
- Button thêm vào giỏ

## Cách sử dụng

### Mở ServiceDetailActivity từ Fragment/Activity:

```java
Intent intent = new Intent(context, ServiceDetailActivity.class);
intent.putExtra("SERVICE_TYPE", "IRONING"); // hoặc WASH_IRON, DRY_CLEAN, MORE
startActivity(intent);
```

### Thêm giá mới cho dịch vụ:

Trong `ServiceDetailActivity.loadServiceItems()`:

```java
case "YOUR_SERVICE":
    serviceItems.add(new ServiceItem("Tên món đồ", giá, "thời gian", R.drawable.icon));
    break;
```

## Bảng giá mẫu

### Dịch vụ ủi đồ (IRONING)
| Món đồ | Giá | Thời gian |
|--------|-----|-----------|
| Áo sơ mi | 15,000đ | 24 giờ |
| Áo thun | 10,000đ | 24 giờ |
| Quần dài | 15,000đ | 24 giờ |
| Quần jean | 18,000đ | 24 giờ |
| Váy/Đầm | 20,000đ | 24 giờ |
| Vest/Com lê | 35,000đ | 24 giờ |
| Áo khoác | 25,000đ | 24 giờ |
| Chân váy | 15,000đ | 24 giờ |

### Dịch vụ giặt và ủi (WASH_IRON)
| Món đồ | Giá | Thời gian |
|--------|-----|-----------|
| Áo sơ mi | 25,000đ | 48 giờ |
| Áo thun | 20,000đ | 48 giờ |
| Quần dài | 25,000đ | 48 giờ |
| Quần jean | 30,000đ | 48 giờ |
| Váy/Đầm | 35,000đ | 48 giờ |
| Vest/Com lê | 60,000đ | 48 giờ |
| Áo khoác | 45,000đ | 48 giờ |
| Áo len | 40,000đ | 48 giờ |
| Chăn/Mền | 80,000đ | 48 giờ |

### Dịch vụ giặt khô (DRY_CLEAN)
| Món đồ | Giá | Thời gian |
|--------|-----|-----------|
| Áo sơ mi | 40,000đ | 72 giờ |
| Quần dài | 45,000đ | 72 giờ |
| Váy/Đầm | 60,000đ | 72 giờ |
| Vest/Com lê | 100,000đ | 72 giờ |
| Áo khoác | 80,000đ | 72 giờ |
| Áo len cao cấp | 70,000đ | 72 giờ |
| Rèm cửa | 150,000đ | 72 giờ |
| Chăn/Mền cao cấp | 200,000đ | 72 giờ |

## Features nổi bật

✅ **UI chuyên nghiệp**
- Material Design với CardView
- Gradient backgrounds
- Smooth animations
- Responsive layout

✅ **Thông tin đầy đủ**
- Tên dịch vụ và mô tả
- Icon trực quan
- Thời gian ước tính
- Giá cả rõ ràng

✅ **UX tốt**
- Click vào từng món để xem chi tiết
- Thêm vào giỏ nhanh
- Hiển thị tổng tiền real-time
- Button đặt hàng nổi bật

✅ **Mở rộng dễ dàng**
- Code structure rõ ràng
- Dễ thêm dịch vụ mới
- Dễ custom giá và thời gian
- Support nhiều loại đồ

## TODO (Tương lai)

- [ ] Tích hợp giỏ hàng thực tế
- [ ] Lưu trữ items đã chọn
- [ ] Thêm quantity picker
- [ ] Express service option
- [ ] Discount codes
- [ ] Payment integration
- [ ] Order tracking

## Changelog

**v1.0 - 2024-12-24**
- Initial release
- 3 dịch vụ chính: Ủi, Giặt+Ủi, Giặt khô
- Danh sách giá đầy đủ
- UI chuyên nghiệp
- Click handlers từ HomeFragment

