package com.project.laundryappui.menu.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;
import com.project.laundryappui.menu.home.home_detail.HomeDetailActivity;
import com.project.laundryappui.menu.home.model.HomeModel;
import com.project.laundryappui.menu.search.adapter.MapsAdapter;
import com.project.laundryappui.utils.JsonDataManager;
import com.project.laundryappui.utils.LocationManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hiển thị bản đồ và tìm kiếm cửa hàng giặt ủi
 * Sử dụng OpenStreetMap (miễn phí, không cần API key)
 */
public class SearchFragment extends Fragment implements MapsAdapter.OnStoreClickListener {
    private static final String TAG = "SearchFragment";
    
    private Context mContext;
    private MapView mapView;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MapsAdapter mapsAdapter;
    
    private JsonDataManager jsonDataManager;
    private LocationManager locationManager;
    
    private List<HomeModel> allStores;
    private List<HomeModel> filteredStores;
    private List<Marker> mapMarkers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        try {
            // Khởi tạo OSMDroid configuration
            Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
            
            initViews(view);
            initManagers();
            
            // Hiển thị bản đồ ngay với vị trí mặc định (không chờ GPS)
            showMapWithDefaultLocation();
            
            // Load location và cập nhật bản đồ sau (async)
            loadStoresAndDisplayOnMap();
            setupSearchView();
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onViewCreated", e);
            Toast.makeText(mContext, "Lỗi khởi tạo bản đồ", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void initViews(View view) {
        mapView = view.findViewById(R.id.map_view);
        searchView = view.findViewById(R.id.search_recipe);
        recyclerView = view.findViewById(R.id.recyclerview_maps);
        
        // Setup MapView
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(
            org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
        );
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(
            new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        );
    }
    
    private void initManagers() {
        jsonDataManager = JsonDataManager.getInstance(mContext);
        locationManager = LocationManager.getInstance(mContext);
    }
    
    /**
     * Hiển thị bản đồ ngay với vị trí mặc định (không chờ GPS)
     */
    private void showMapWithDefaultLocation() {
        // Lấy vị trí mặc định (TP.HCM)
        double[] defaultLocation = locationManager.getDefaultLocation();
        double defaultLat = defaultLocation[0];
        double defaultLng = defaultLocation[1];
        
        // Load stores ngay (không cần sort theo khoảng cách)
        allStores = jsonDataManager.getAllStores();
        filteredStores = new ArrayList<>(allStores);
        
        // Hiển thị bản đồ ngay với vị trí mặc định
        centerMapToLocation(defaultLat, defaultLng, 12.0);
        displayStoresOnMap();
        setupRecyclerView();
        
        Log.d(TAG, "Map displayed with default location immediately");
    }
    
    /**
     * Load danh sách cửa hàng và cập nhật bản đồ với vị trí thực tế
     */
    private void loadStoresAndDisplayOnMap() {
        locationManager.getCurrentLocation((userLat, userLng) -> {
            Log.d(TAG, "User location received: " + userLat + ", " + userLng);
            
            // Load và sort stores theo khoảng cách từ vị trí thực tế
            allStores = jsonDataManager.getStoresSortedByDistance(userLat, userLng);
            filteredStores = new ArrayList<>(allStores);
            
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // Cập nhật markers trên bản đồ
                    displayStoresOnMap();
                    
                    // Center map về vị trí user thực tế (smooth animation)
                    centerMapToLocation(userLat, userLng, 12.0);
                    
                    // Cập nhật adapter với danh sách đã sort
                    if (mapsAdapter != null) {
                        mapsAdapter.updateStores(filteredStores);
                    } else {
                        setupRecyclerView();
                    }
                    
                    Log.d(TAG, "Map updated with user location");
                });
            }
        });
    }
    
    /**
     * Hiển thị các marker của cửa hàng trên bản đồ
     */
    private void displayStoresOnMap() {
        // Clear markers cũ
        for (Marker marker : mapMarkers) {
            mapView.getOverlays().remove(marker);
        }
        mapMarkers.clear();
        
        // Thêm marker mới cho mỗi cửa hàng
        for (HomeModel store : filteredStores) {
            addStoreMarker(store);
        }
        
        mapView.invalidate();
        Log.d(TAG, "Added " + mapMarkers.size() + " markers to map");
    }
    
    /**
     * Thêm marker cho một cửa hàng
     */
    private void addStoreMarker(HomeModel store) {
        GeoPoint point = new GeoPoint(store.getLatitude(), store.getLongitude());
        Marker marker = new Marker(mapView);
        
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(store.getName());
        marker.setSnippet(store.getAddress() + "\n" + store.getPrice() + "\nRating: " + store.getRating());
        
        // Custom icon với size phù hợp (32x32 dp)
        try {
            Drawable icon = ContextCompat.getDrawable(mContext, R.drawable.ic_location);
            if (icon != null) {
                // Scale icon xuống size 32x32 dp để không che bản đồ
                int width = dpToPx(32);
                int height = dpToPx(32);
                Bitmap scaledBitmap = drawableToBitmap(icon, width, height);
                marker.setIcon(new BitmapDrawable(getResources(), scaledBitmap));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting marker icon", e);
        }
        
        // Click listener
        marker.setOnMarkerClickListener((clickedMarker, mapView) -> {
            // Hiển thị info window
            clickedMarker.showInfoWindow();
            
            // Center map về marker
            IMapController mapController = mapView.getController();
            mapController.animateTo(clickedMarker.getPosition());
            
            return true;
        });
        
        mapView.getOverlays().add(marker);
        mapMarkers.add(marker);
    }
    
    /**
     * Center map về một vị trí (có animation mượt mà)
     */
    private void centerMapToLocation(double lat, double lng, double zoom) {
        if (mapView == null) return;
        
        IMapController mapController = mapView.getController();
        mapController.setZoom(zoom);
        GeoPoint targetPoint = new GeoPoint(lat, lng);
        
        // Sử dụng animateTo để có hiệu ứng mượt mà
        mapController.animateTo(targetPoint);
    }
    
    /**
     * Setup RecyclerView với adapter
     */
    private void setupRecyclerView() {
        mapsAdapter = new MapsAdapter(filteredStores, this);
        recyclerView.setAdapter(mapsAdapter);
    }
    
    /**
     * Setup SearchView để tìm kiếm
     */
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Reset về tất cả cửa hàng
                    filteredStores = new ArrayList<>(allStores);
                } else {
                    performSearch(newText);
                }
                return true;
            }
        });
    }
    
    /**
     * Thực hiện tìm kiếm
     */
    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredStores = new ArrayList<>(allStores);
        } else {
            filteredStores = jsonDataManager.searchStores(query);
        }
        
        // Cập nhật UI
        if (mapsAdapter != null) {
            mapsAdapter.updateStores(filteredStores);
        }
        
        displayStoresOnMap();
        
        Log.d(TAG, "Search for: " + query + ", found: " + filteredStores.size() + " stores");
        
        // Center map về cửa hàng đầu tiên trong kết quả
        if (!filteredStores.isEmpty()) {
            HomeModel firstStore = filteredStores.get(0);
            centerMapToLocation(firstStore.getLatitude(), firstStore.getLongitude(), 14.0);
        }
    }
    
    /**
     * Callback khi click vào item trong RecyclerView
     */
    @Override
    public void onStoreClick(HomeModel store) {
        // Center map về cửa hàng
        centerMapToLocation(store.getLatitude(), store.getLongitude(), 16.0);
        
        // Tìm và show info window của marker tương ứng
        for (Marker marker : mapMarkers) {
            if (marker.getTitle().equals(store.getName())) {
                marker.showInfoWindow();
                break;
            }
        }
        
        // Mở detail activity
        Intent intent = new Intent(mContext, HomeDetailActivity.class);
        intent.putExtra("STORE_ID", store.getId());
        startActivity(intent);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    
    /**
     * Chuyển đổi dp sang pixel
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * Chuyển đổi Drawable thành Bitmap với size cụ thể
     */
    private Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), width, height, true);
            }
        }
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
