package com.project.laundryappui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BookingConfirmedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvOrderId;
    private TextView tvOrderTime;
    private TextView tvWorkerName;
    private TextView tvWorkerPhone;
    private TextView tvWorkerLocation;
    private TextView tvServiceType;
    private TextView tvServicePrice;
    private TextView tvCustomerAddress;
    private TextView tvPaymentMethod;
    private TextView tvTotalPrice;
    private TextView tvEstimatedTime;
    private Button btnTrackOrder;
    private Button btnBackToHome;

    // Order data
    private String orderId;
    private String workerName;
    private String workerPhone;
    private String workerLocation;
    private String serviceType;
    private String servicePrice;
    private String customerAddress;
    private String paymentMethod;
    private String totalPrice;
    private String estimatedTime = "15 phút";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        // Get data from intent
        getIntentData();

        initializeViews();
        setupToolbar();
        setupOrderData();
        setupClickListeners();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            workerName = intent.getStringExtra("worker_name");
            workerPhone = intent.getStringExtra("worker_phone");
            serviceType = intent.getStringExtra("service");
            customerAddress = intent.getStringExtra("address");
            paymentMethod = intent.getStringExtra("payment");
            totalPrice = intent.getStringExtra("total_price");
            
            // Generate order ID
            orderId = generateOrderId();
            
            // Set default values if not provided
            if (workerName == null) workerName = "Nguyễn Văn A";
            if (workerPhone == null) workerPhone = "0901234567";
            if (workerLocation == null) workerLocation = "Quận 1, TP.HCM";
            if (serviceType == null) serviceType = "Đánh giày sneaker";
            if (servicePrice == null) servicePrice = "50.000đ";
            if (customerAddress == null) customerAddress = "Chưa có địa chỉ";
            if (paymentMethod == null) paymentMethod = "Tiền mặt";
            if (totalPrice == null) totalPrice = "50.000đ";
            
            // Map service type to display name
            serviceType = mapServiceTypeToDisplayName(serviceType);
            
            // Map payment method to display name
            paymentMethod = mapPaymentMethodToDisplayName(paymentMethod);
        }
    }

    private String generateOrderId() {
        Random random = new Random();
        int orderNumber = 100000 + random.nextInt(900000);
        return "ORD" + orderNumber;
    }

    private String mapServiceTypeToDisplayName(String service) {
        switch (service) {
            case "sneakers":
                return "Đánh giày sneaker";
            case "leather":
                return "Đánh giày da";
            case "high_heels":
                return "Đánh giày cao gót";
            default:
                return service;
        }
    }

    private String mapPaymentMethodToDisplayName(String payment) {
        switch (payment) {
            case "cash":
                return "Tiền mặt";
            case "ewallet":
                return "Ví điện tử";
            default:
                return payment;
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvOrderTime = findViewById(R.id.tv_order_time);
        tvWorkerName = findViewById(R.id.tv_worker_name);
        tvWorkerPhone = findViewById(R.id.tv_worker_phone);
        tvWorkerLocation = findViewById(R.id.tv_worker_location);
        tvServiceType = findViewById(R.id.tv_service_type);
        tvServicePrice = findViewById(R.id.tv_service_price);
        tvCustomerAddress = findViewById(R.id.tv_customer_address);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvEstimatedTime = findViewById(R.id.tv_estimated_time);
        btnTrackOrder = findViewById(R.id.btn_track_order);
        btnBackToHome = findViewById(R.id.btn_back_to_home);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupOrderData() {
        // Set order ID
        tvOrderId.setText(orderId);
        
        // Set order time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        tvOrderTime.setText(currentTime);
        
        // Set worker info
        tvWorkerName.setText(workerName);
        tvWorkerPhone.setText(workerPhone);
        tvWorkerLocation.setText(workerLocation);
        
        // Set service info
        tvServiceType.setText(serviceType);
        tvServicePrice.setText(totalPrice);
        
        // Set customer address
        tvCustomerAddress.setText(customerAddress);
        
        // Set payment method
        tvPaymentMethod.setText(paymentMethod);
        
        // Set total price
        tvTotalPrice.setText(totalPrice);
        
        // Set estimated time
        tvEstimatedTime.setText(estimatedTime);
    }

    private void setupClickListeners() {
        btnTrackOrder.setOnClickListener(v -> {
            // TODO: Navigate to order tracking screen
            // Intent intent = new Intent(this, OrderTrackingActivity.class);
            // intent.putExtra("order_id", orderId);
            // startActivity(intent);
            finish();
        });

        btnBackToHome.setOnClickListener(v -> {
            // Navigate to home and clear back stack
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Navigate to home when back is pressed
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

