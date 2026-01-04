package com.project.laundryappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Activity cho phép người dùng chọn vị trí trên bản đồ OpenStreetMap
 */
public class MapLocationPickerActivity extends AppCompatActivity implements MapEventsReceiver {

    private static final String TAG = "MapLocationPickerActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // UI Components
    private Toolbar toolbar;
    private MapView mapView;
    private CardView locationInfoCard;
    private TextView tvSelectedAddress, tvCoordinates, tvBottomAddress;
    private Button btnConfirmLocation, btnSearchMap;
    private EditText etSearchLocation;
    private FloatingActionButton fabCurrentLocation;

    // Location Services
    private FusedLocationProviderClient fusedLocationClient;
    private IMapController mapController;

    // Map State
    private GeoPoint selectedLocation = null;
    private GeoPoint currentLocation = null;
    private boolean isLocationSelected = false;

    // Reverse Geocoding
    private Handler geocodingHandler = new Handler(Looper.getMainLooper());
    private Runnable geocodingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_picker);

        // Initialize OSMDroid configuration
        Configuration.getInstance().setUserAgentValue(getPackageName());

        initializeViews();
        setupToolbar();
        setupMap();
        setupClickListeners();

        // Check if there's an initial location passed from caller
        handleIntentExtras();
        
        // Request location permission and get GPS
        requestLocationPermission();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        mapView = findViewById(R.id.map_view);
        locationInfoCard = findViewById(R.id.location_info_card);
        tvSelectedAddress = findViewById(R.id.tv_selected_address);
        tvCoordinates = findViewById(R.id.tv_coordinates);
        tvBottomAddress = findViewById(R.id.tv_bottom_address);
        btnConfirmLocation = findViewById(R.id.btn_confirm_location);
        btnSearchMap = findViewById(R.id.btn_search_map);
        etSearchLocation = findViewById(R.id.et_search_location);
        fabCurrentLocation = findViewById(R.id.fab_current_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupMap() {
        // Configure MapView
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(
                org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
        );

        mapController = mapView.getController();
        mapController.setZoom(15.0);

        // Add map events overlay to handle tap events
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        mapView.getOverlays().add(0, mapEventsOverlay);

        // Add map listener for scroll/zoom events
        mapView.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                updateLocationFromMapCenter();
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return false;
            }
        });

        // DON'T set default location yet - wait for GPS
        // Will be set in getCurrentLocation() or fallback to HCM if GPS fails
    }

    private void setupClickListeners() {
        // FAB to center on current location
        fabCurrentLocation.setOnClickListener(v -> centerOnCurrentLocation());

        // Confirm location button
        btnConfirmLocation.setOnClickListener(v -> confirmLocation());

        // Search button
        btnSearchMap.setOnClickListener(v -> searchLocation());

        // Search on enter key
        etSearchLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation();
                return true;
            }
            return false;
        });
    }

    private void handleIntentExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            double lat = intent.getDoubleExtra("latitude", 0);
            double lng = intent.getDoubleExtra("longitude", 0);
            if (lat != 0 && lng != 0) {
                // User has previously selected a location
                selectedLocation = new GeoPoint(lat, lng);
                centerMapToLocation(selectedLocation, 16.0);
                updateLocationInfo(selectedLocation);
                isLocationSelected = true;
                updateConfirmButton();
                
                // Don't get GPS location since user already has a saved location
                Log.d(TAG, "Using previously selected location from intent");
            }
            // If no previous location, getCurrentLocation() will be called automatically
        }
    }

    private void requestLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission first
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            
            // Fallback to default location while waiting for permission
            GeoPoint fallbackLocation = new GeoPoint(10.7906, 106.7009);
            centerMapToLocation(fallbackLocation, 13.0);
        } else {
            // Only get GPS if no previous location was loaded from intent
            if (!isLocationSelected) {
                getCurrentLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fabCurrentLocation.setEnabled(false);
        
        // Show loading message
        Toast.makeText(this, "Đang lấy vị trí hiện tại...", Toast.LENGTH_SHORT).show();

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    fabCurrentLocation.setEnabled(true);

                    if (location != null) {
                        currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "GPS location: " + location.getLatitude() + ", " + location.getLongitude());

                        // ALWAYS center to GPS location
                        centerMapToLocation(currentLocation, 16.0);
                        
                        // Auto update location info
                        updateLocationInfo(currentLocation);
                        isLocationSelected = true;
                        updateConfirmButton();
                        
                        Toast.makeText(this, "Đã lấy vị trí GPS hiện tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "GPS location is null, using fallback location");
                        // Fallback to Học Viện Công Nghệ Bưu Chính Viễn Thông
                        GeoPoint fallbackLocation = new GeoPoint(10.7906, 106.7009);
                        centerMapToLocation(fallbackLocation, 13.0);
                        updateLocationInfo(fallbackLocation);
                        isLocationSelected = true;
                        updateConfirmButton();
                        Toast.makeText(this, "Không thể lấy GPS. Sử dụng vị trí mặc định.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    fabCurrentLocation.setEnabled(true);
                    Log.e(TAG, "Error getting GPS location", e);
                    
                    // Fallback to Học Viện Công Nghệ Bưu Chính Viễn Thông
                    GeoPoint fallbackLocation = new GeoPoint(10.7906, 106.7009);
                    centerMapToLocation(fallbackLocation, 13.0);
                    updateLocationInfo(fallbackLocation);
                    isLocationSelected = true;
                    updateConfirmButton();
                    Toast.makeText(this, "Lỗi GPS. Sử dụng vị trí mặc định.", Toast.LENGTH_LONG).show();
                });
    }

    private void centerOnCurrentLocation() {
        if (currentLocation != null) {
            centerMapToLocation(currentLocation, 16.0);
            updateLocationInfo(currentLocation);
            isLocationSelected = true;
            updateConfirmButton();
        } else {
            // Try to get current location again
            getCurrentLocation();
        }
    }

    private void centerMapToLocation(GeoPoint location, double zoom) {
        mapController.setZoom(zoom);
        mapController.animateTo(location);
    }

    private void updateLocationFromMapCenter() {
        if (mapView != null) {
            GeoPoint center = (GeoPoint) mapView.getMapCenter();
            updateLocationInfo(center);
            isLocationSelected = true;
            updateConfirmButton();
        }
    }

    private void updateLocationInfo(GeoPoint location) {
        selectedLocation = location;

        // Update coordinates display
        tvCoordinates.setText(String.format(Locale.getDefault(),
                "%.6f, %.6f", location.getLatitude(), location.getLongitude()));

        // Show info card
        locationInfoCard.setVisibility(View.VISIBLE);

        // Perform reverse geocoding with debounce
        performReverseGeocoding(location);
    }

    private void performReverseGeocoding(GeoPoint location) {
        // Cancel previous geocoding request
        if (geocodingRunnable != null) {
            geocodingHandler.removeCallbacks(geocodingRunnable);
        }

        // Debounce geocoding requests (wait 500ms after last change)
        geocodingRunnable = () -> {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1
                );

                runOnUiThread(() -> {
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String addressText = buildAddressString(address);

                        // Check if this is Google's default address and replace with fallback
                        if (isGoogleDefaultAddress(addressText)) {
                            addressText = getString(R.string.fallback_address);
                        }

                        tvSelectedAddress.setText(addressText);
                        tvBottomAddress.setText(addressText);
                    } else {
                        // Use fallback address when geocoding fails
                        String fallbackText = getString(R.string.fallback_address);
                        tvSelectedAddress.setText(fallbackText);
                        tvBottomAddress.setText(fallbackText);
                    }
                });

            } catch (IOException e) {
                Log.e(TAG, "Error in reverse geocoding", e);
                runOnUiThread(() -> {
                    // Use fallback address when geocoding fails
                    String fallbackText = getString(R.string.fallback_address);
                    tvSelectedAddress.setText(fallbackText);
                    tvBottomAddress.setText(fallbackText);
                });
            }
        };

        geocodingHandler.postDelayed(geocodingRunnable, 500);
    }

    private String buildAddressString(Address address) {
        StringBuilder sb = new StringBuilder();

        // Try to build a readable address
        if (address.getAddressLine(0) != null) {
            sb.append(address.getAddressLine(0));
        } else {
            // Fallback to components
            if (address.getLocality() != null) {
                sb.append(address.getLocality());
            }
            if (address.getSubAdminArea() != null) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(address.getSubAdminArea());
            }
            if (address.getAdminArea() != null) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(address.getAdminArea());
            }
            if (address.getCountryName() != null) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(address.getCountryName());
            }
        }

        return sb.toString();
    }

    /**
     * Check if the address is Google's default location (1600 Amphitheatre Parkway)
     */
    private boolean isGoogleDefaultAddress(String address) {
        if (address == null) return false;

        // Check for common variations of Google's address
        String lowerAddress = address.toLowerCase();
        return lowerAddress.contains("1600 amphitheatre parkway") ||
               lowerAddress.contains("mountain view") ||
               lowerAddress.contains("california") ||
               lowerAddress.contains("94043");
    }

    private void updateConfirmButton() {
        btnConfirmLocation.setEnabled(isLocationSelected);
    }

    private void confirmLocation() {
        if (selectedLocation == null) {
            Toast.makeText(this, "Vui lòng chọn vị trí trước", Toast.LENGTH_SHORT).show();
            return;
        }

        // Return result to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("latitude", selectedLocation.getLatitude());
        resultIntent.putExtra("longitude", selectedLocation.getLongitude());
        resultIntent.putExtra("address", tvBottomAddress.getText().toString());

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void searchLocation() {
        String searchQuery = etSearchLocation.getText().toString().trim();
        
        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa điểm cần tìm", Toast.LENGTH_SHORT).show();
            etSearchLocation.requestFocus();
            return;
        }

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etSearchLocation.getWindowToken(), 0);
        }

        // Disable search button
        btnSearchMap.setEnabled(false);
        btnSearchMap.setText("Đang tìm...");

        // Forward geocoding - convert address to coordinates
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this, java.util.Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocationName(searchQuery, 1);

                runOnUiThread(() -> {
                    btnSearchMap.setEnabled(true);
                    btnSearchMap.setText("Tìm");

                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        GeoPoint location = new GeoPoint(address.getLatitude(), address.getLongitude());
                        
                        // Center map to found location
                        centerMapToLocation(location, 16.0);
                        
                        // Update location info
                        updateLocationInfo(location);
                        isLocationSelected = true;
                        updateConfirmButton();

                        Toast.makeText(this, "Đã tìm thấy: " + searchQuery, Toast.LENGTH_SHORT).show();
                        
                        // Clear search box
                        etSearchLocation.setText("");
                    } else {
                        Toast.makeText(this, "Không tìm thấy địa điểm: " + searchQuery, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                Log.e(TAG, "Error searching location", e);
                runOnUiThread(() -> {
                    btnSearchMap.setEnabled(true);
                    btnSearchMap.setText("Tìm");
                    Toast.makeText(this, "Lỗi khi tìm kiếm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        // Handle single tap on map
        updateLocationInfo(p);
        isLocationSelected = true;
        updateConfirmButton();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        // Handle long press on map (same as single tap for selection)
        updateLocationInfo(p);
        isLocationSelected = true;
        updateConfirmButton();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, getString(R.string.location_permission_required), Toast.LENGTH_LONG).show();
            }
        }
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

        // Cancel any pending geocoding
        if (geocodingRunnable != null) {
            geocodingHandler.removeCallbacks(geocodingRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
}