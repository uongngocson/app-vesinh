# 🗺️ Search Map Feature - Hướng Dẫn Sử Dụng

## 📍 Tổng Quan

Search Map là tính năng tìm kiếm và hiển thị các cửa hàng giặt ủi YIBO.VN trên bản đồ thực tế.

### ✨ Tính Năng Chính:
- **Bản đồ thực tế**: Sử dụng OpenStreetMap (100% miễn phí, không cần API key)
- **30+ markers**: Hiển thị tất cả cửa hàng YIBO.VN trên TP.HCM
- **Real-time search**: Tìm kiếm theo tên hoặc quận
- **Auto-sort**: Sắp xếp theo khoảng cách từ vị trí user
- **Interactive**: Click marker xem thông tin, click item mở detail

## 🏗️ Kiến Trúc

### 1. **OpenStreetMap (osmdroid)**
```gradle
implementation 'org.osmdroid:osmdroid-android:6.1.16'
```

**Ưu điểm:**
- ✅ Hoàn toàn miễn phí
- ✅ Không cần API key
- ✅ Không giới hạn số lượng request
- ✅ Open source, community support tốt
- ✅ Offline map support

### 2. **SearchFragment.java**
```java
- MapView: Hiển thị bản đồ
- SearchView: Tìm kiếm
- RecyclerView: Danh sách cửa hàng
- JsonDataManager: Load data từ JSON
- LocationManager: Lấy vị trí user
```

### 3. **MapsAdapter.java**
```java
- Adapter cho RecyclerView
- Hiển thị: tên, rating, district, distance
- Click listener → center map + open detail
```

### 4. **Layout**
```xml
fragment_search.xml:
- MapView (osmdroid)
- SearchView
- RecyclerView (horizontal scroll)

item_maps.xml:
- CardView với dark background
- Name, Rating, Location, Status
```

## 🎯 Flow Hoạt Động

```
User mở SearchFragment
    ↓
Lấy vị trí GPS user
    ↓
Load 30 stores từ JSON
    ↓
Sort theo khoảng cách
    ↓
Hiển thị markers trên map
    ↓
Center map về vị trí user
    ↓
Hiển thị RecyclerView bên dưới
    ↓
User có thể:
    - Pan/Zoom map
    - Click marker → show info
    - Search by name/district
    - Click item → center map + open detail
```

## 🔍 Search Functionality

### **Search by Name:**
```java
Input: "YIBO"
Result: All stores with "YIBO" in name
```

### **Search by District:**
```java
Input: "Quận 1"
Result: All stores in Quận 1
```

### **Search by Keyword:**
```java
Input: "Thủ Đức"
Result: All stores in Thủ Đức
```

### **Empty Search:**
```java
Input: ""
Result: Show all 30 stores
```

## 📱 User Interactions

### 1. **Pan & Zoom Map**
- 2 fingers: Zoom in/out
- 1 finger: Pan around
- Zoom buttons: Top-right corner

### 2. **Click Marker**
```
Click marker on map
    ↓
Show info window (name, address, price, rating)
    ↓
Center map to marker
```

### 3. **Search**
```
Type in SearchView
    ↓
Filter stores real-time
    ↓
Update markers on map
    ↓
Update RecyclerView list
    ↓
Center to first result
```

### 4. **Click Item in RecyclerView**
```
Click store item
    ↓
Center map to store
    ↓
Show marker info window
    ↓
Open HomeDetailActivity
```

## 🎨 UI/UX Design

### **Map View:**
- Full screen map
- Search bar on top with rounded corners
- RecyclerView at bottom (horizontal scroll)
- Clean, modern design

### **Store Card (item_maps.xml):**
```
┌─────────────────────────────┐
│ Store Name          Rating  │
│                     "Rating" │
│ District - Distance         │
│ Status              "Open"  │
└─────────────────────────────┘
```

### **Marker Info Window:**
```
┌─────────────────────┐
│ YIBO.VN Quận 1     │
│ 123 Nguyễn Huệ...  │
│ 350k - 690k VND    │
│ Rating: 4.8        │
└─────────────────────┘
```

## 🚀 Implementation Details

### **MapView Setup:**
```java
mapView.setTileSource(TileSourceFactory.MAPNIK);
mapView.setMultiTouchControls(true);
mapView.getZoomController().setVisibility(...);
```

### **Add Marker:**
```java
GeoPoint point = new GeoPoint(lat, lng);
Marker marker = new Marker(mapView);
marker.setPosition(point);
marker.setTitle(name);
marker.setSnippet(info);
marker.setIcon(icon);
mapView.getOverlays().add(marker);
```

### **Search Filter:**
```java
searchView.setOnQueryTextListener(new OnQueryTextListener() {
    @Override
    public boolean onQueryTextChange(String text) {
        performSearch(text);
        return true;
    }
});
```

### **Center Map:**
```java
IMapController controller = mapView.getController();
controller.setZoom(14.0);
controller.setCenter(new GeoPoint(lat, lng));
```

## 📊 Data Flow

### **Load Stores:**
```java
locationManager.getCurrentLocation((lat, lng) -> {
    allStores = jsonDataManager.getStoresSortedByDistance(lat, lng);
    displayStoresOnMap();
    setupRecyclerView();
});
```

### **Display Markers:**
```java
for (HomeModel store : stores) {
    GeoPoint point = new GeoPoint(store.getLatitude(), store.getLongitude());
    Marker marker = new Marker(mapView);
    // ... setup marker
    mapView.getOverlays().add(marker);
}
```

### **Filter Search:**
```java
filteredStores = jsonDataManager.searchStores(query);
mapsAdapter.updateStores(filteredStores);
displayStoresOnMap(); // Re-draw markers
```

## 🔧 Configuration

### **AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

### **build.gradle:**
```gradle
implementation 'org.osmdroid:osmdroid-android:6.1.16'
implementation 'com.google.android.gms:play-services-location:21.0.1'
```

## 💡 Best Practices

### 1. **Lifecycle Management**
```java
@Override
public void onResume() {
    mapView.onResume(); // Resume map rendering
}

@Override
public void onPause() {
    mapView.onPause(); // Pause to save battery
}

@Override
public void onDestroyView() {
    mapView.onDetach(); // Clean up resources
}
```

### 2. **Memory Management**
- Clear old markers before adding new ones
- Reuse adapter instead of creating new
- Use weak references for callbacks

### 3. **User Experience**
- Show loading state while fetching location
- Center to first search result
- Smooth animations when centering map
- Info windows for quick info

### 4. **Error Handling**
- Fallback to default location if GPS fails
- Toast messages for user feedback
- Try-catch around map operations
- Graceful degradation

## 🎯 Features Breakdown

### ✅ **Đã Implement:**
- OpenStreetMap integration
- 30 store markers
- Search by name/district
- Click marker to show info
- Click item to open detail
- Auto-sort by distance
- Real-time filter
- Horizontal RecyclerView

### 🔄 **Có Thể Mở Rộng:**
- Cluster markers (nhiều marker gần nhau → cluster)
- Route navigation (từ user đến store)
- Filter by rating/price
- Favorite stores
- Offline map download
- Custom marker colors by rating
- Heat map by density

## 📝 Troubleshooting

### **Map không hiển thị?**
- Check INTERNET permission
- Check osmdroid dependency
- Check Configuration.getInstance()

### **Markers không xuất hiện?**
- Check latitude/longitude values
- Check mapView.invalidate()
- Check marker icon resource

### **Search không hoạt động?**
- Check JsonDataManager.searchStores()
- Check query text listener
- Check adapter.updateStores()

### **App crash khi mở map?**
- Check WRITE_EXTERNAL_STORAGE permission
- Check mapView lifecycle methods
- Check try-catch around map operations

## 🌟 Tips & Tricks

1. **Performance**: Limit markers < 100 để smooth
2. **Battery**: Pause map khi không dùng
3. **UX**: Show loading spinner khi load stores
4. **Search**: Debounce search input (300ms delay)
5. **Offline**: Cache tiles cho offline use

## 📞 Support

- **OSMDroid Docs**: https://osmdroid.github.io/osmdroid/
- **OpenStreetMap**: https://www.openstreetmap.org/
- **GitHub Issues**: Report bugs và suggestions

---

**Version:** 1.0  
**Last Updated:** December 2025  
**Tech Stack:** Android Java + osmdroid + OpenStreetMap

