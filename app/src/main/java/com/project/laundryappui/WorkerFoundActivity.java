package com.project.laundryappui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.laundryappui.model.Worker;
import com.project.laundryappui.utils.WorkerDataManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class WorkerFoundActivity extends AppCompatActivity {
    
    private static final String TAG = "WorkerFoundActivity";

    private Toolbar toolbar;
    private ImageView ivWorkerAvatar;
    private TextView tvWorkerName;
    private TextView tvWorkerRating;
    private TextView tvWorkerLocation;
    private TextView tvCompletedOrders;
    private TextView tvResponseTime;
    private TextView tvDistance;
    private TextView tvEstimatedArrival;
    private Button btnCallWorker;
    private Button btnMessageWorker;
    private Button btnConfirmBooking;
    private TextView tvTotalPrice;
    private MapView mapView;

    // Worker data - loaded from JSON
    private Worker selectedWorker;
    private WorkerDataManager workerDataManager;

    // Booking data from previous activity
    private String selectedService;
    private String selectedPayment;
    private String customerAddress;
    private double customerLatitude;
    private double customerLongitude;
    private String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_found);

        // Initialize OSMDroid configuration
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Initialize WorkerDataManager
        workerDataManager = WorkerDataManager.getInstance(this);

        // Get data from intent
        getIntentData();

        // Find and load worker data from JSON
        findAndLoadWorker();

        initializeViews();
        setupToolbar();
        setupWorkerData();
        setupClickListeners();
        setupMap(savedInstanceState);
    }
    
    /**
     * Tìm và load thợ phù hợp nhất từ JSON
     */
    private void findAndLoadWorker() {
        if (customerLatitude != 0 && customerLongitude != 0 && selectedService != null) {
            // Tìm thợ tốt nhất dựa trên vị trí và loại dịch vụ
            selectedWorker = workerDataManager.findBestWorker(
                customerLatitude, 
                customerLongitude, 
                selectedService
            );
        } else {
            // Nếu không có vị trí hoặc dịch vụ, lấy thợ ngẫu nhiên
            selectedWorker = workerDataManager.getRandomWorker();
        }
        
        // Fallback nếu không tìm thấy thợ
        if (selectedWorker == null) {
            Log.w(TAG, "No worker found, using default worker");
            // Tạo worker mặc định để tránh crash
            selectedWorker = createDefaultWorker();
        }
    }
    
    /**
     * Tạo worker mặc định khi không tìm thấy thợ từ JSON
     */
    private Worker createDefaultWorker() {
        Worker defaultWorker = new Worker();
        defaultWorker.setId("default_worker");
        defaultWorker.setName("Nguyễn Văn A");
        defaultWorker.setPhone("0901234567");
        defaultWorker.setRating(4.8);
        defaultWorker.setCompletedOrders(156);
        defaultWorker.setResponseTime("5 phút");
        defaultWorker.setLocation("Quận 1, TP.HCM");
        defaultWorker.setLatitude(10.7769);
        defaultWorker.setLongitude(106.7009);
        defaultWorker.setDistrict("Quận 1");
        defaultWorker.setCity("TP.HCM");
        defaultWorker.setVerified(true);
        defaultWorker.setAvatar("ic_avatar");
        defaultWorker.setSpecialties(new String[]{"sneakers", "leather", "high_heels"});
        defaultWorker.setExperienceYears(5);
        return defaultWorker;
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            selectedService = intent.getStringExtra("service");
            selectedPayment = intent.getStringExtra("payment");
            customerAddress = intent.getStringExtra("address");
            customerLatitude = intent.getDoubleExtra("latitude", 0);
            customerLongitude = intent.getDoubleExtra("longitude", 0);
            totalPrice = intent.getStringExtra("total_price");
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        ivWorkerAvatar = findViewById(R.id.iv_worker_avatar);
        tvWorkerName = findViewById(R.id.tv_worker_name);
        tvWorkerRating = findViewById(R.id.tv_worker_rating);
        tvWorkerLocation = findViewById(R.id.tv_worker_location);
        tvCompletedOrders = findViewById(R.id.tv_completed_orders);
        tvResponseTime = findViewById(R.id.tv_response_time);
        tvDistance = findViewById(R.id.tv_distance);
        tvEstimatedArrival = findViewById(R.id.tv_estimated_arrival);
        btnCallWorker = findViewById(R.id.btn_call_worker);
        btnMessageWorker = findViewById(R.id.btn_message_worker);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        mapView = findViewById(R.id.map_view);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupWorkerData() {
        if (selectedWorker == null) {
            Log.e(TAG, "Worker is null, cannot setup UI");
            return;
        }
        
        // Set worker name
        tvWorkerName.setText(selectedWorker.getName());
        
        // Set worker rating
        tvWorkerRating.setText(String.format("%.1f ⭐", selectedWorker.getRating()));
        
        // Set worker location
        tvWorkerLocation.setText(selectedWorker.getLocation());
        
        // Set completed orders
        tvCompletedOrders.setText(String.format("%d đơn", selectedWorker.getCompletedOrders()));
        
        // Set response time
        tvResponseTime.setText(selectedWorker.getResponseTime());
        
        // Calculate and set distance
        if (customerLatitude != 0 && customerLongitude != 0) {
            String distanceText = selectedWorker.getFormattedDistance(customerLatitude, customerLongitude);
            tvDistance.setText(distanceText);
        } else {
            tvDistance.setText("--");
        }
        
        // Calculate and set estimated arrival time
        if (customerLatitude != 0 && customerLongitude != 0) {
            String estimatedArrivalText = selectedWorker.getEstimatedArrivalTime(customerLatitude, customerLongitude);
            tvEstimatedArrival.setText(estimatedArrivalText);
        } else {
            tvEstimatedArrival.setText("15 phút");
        }
        
        // Set total price from intent
        if (totalPrice != null && !totalPrice.isEmpty()) {
            tvTotalPrice.setText(totalPrice);
        }
    }

    private void setupClickListeners() {
        btnCallWorker.setOnClickListener(v -> {
            if (selectedWorker != null) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + selectedWorker.getPhone()));
                startActivity(callIntent);
            }
        });

        btnMessageWorker.setOnClickListener(v -> {
            // TODO: Navigate to message screen
            // if (selectedWorker != null) {
            //     Intent messageIntent = new Intent(this, MessageActivity.class);
            //     messageIntent.putExtra("worker_id", selectedWorker.getId());
            //     startActivity(messageIntent);
            // }
        });

        btnConfirmBooking.setOnClickListener(v -> {
            if (selectedWorker == null) {
                return;
            }
            
            // Navigate to BookingConfirmedActivity
            Intent intent = new Intent(this, BookingConfirmedActivity.class);
            intent.putExtra("worker_name", selectedWorker.getName());
            intent.putExtra("worker_phone", selectedWorker.getPhone());
            intent.putExtra("worker_location", selectedWorker.getLocation());
            intent.putExtra("service", selectedService);
            intent.putExtra("address", customerAddress);
            intent.putExtra("payment", selectedPayment);
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
            finish();
        });
    }

    private void setupMap(Bundle savedInstanceState) {
        if (mapView == null || selectedWorker == null) {
            return;
        }
        
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(13.0);

        // Add marker for worker location
        GeoPoint workerLocation = new GeoPoint(
            selectedWorker.getLatitude(), 
            selectedWorker.getLongitude()
        );
        Marker workerMarker = new Marker(mapView);
        workerMarker.setPosition(workerLocation);
        workerMarker.setTitle(selectedWorker.getName());
        workerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(workerMarker);

        // Add marker for customer location if available
        if (customerLatitude != 0 && customerLongitude != 0) {
            GeoPoint customerLocation = new GeoPoint(customerLatitude, customerLongitude);
            Marker customerMarker = new Marker(mapView);
            customerMarker.setPosition(customerLocation);
            customerMarker.setTitle("Vị trí của bạn");
            customerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(customerMarker);

            // Center camera between two points
            GeoPoint center = new GeoPoint(
                    (selectedWorker.getLatitude() + customerLatitude) / 2,
                    (selectedWorker.getLongitude() + customerLongitude) / 2
            );
            mapView.getController().setCenter(center);
            mapView.getController().setZoom(12.0);
        } else {
            mapView.getController().setCenter(workerLocation);
            mapView.getController().setZoom(14.0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

