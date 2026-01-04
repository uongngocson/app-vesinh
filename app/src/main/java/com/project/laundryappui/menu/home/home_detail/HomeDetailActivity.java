package com.project.laundryappui.menu.home.home_detail;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.laundryappui.R;
import com.project.laundryappui.menu.home.model.HomeModel;
import com.project.laundryappui.utils.JsonDataManager;

/**
 * Activity hiển thị chi tiết cửa hàng giặt ủi
 * Nhận storeId từ Intent và load dữ liệu từ JSON
 */
public class HomeDetailActivity extends AppCompatActivity {
    private static final String TAG = "HomeDetailActivity";
    
    private ImageButton buttonBack;
    private ImageView imageStore;
    private TextView textName;
    private TextView textLocation;
    private TextView textRate;
    private RatingBar ratingBar;
    private ImageButton buttonPhone;
    private ImageButton buttonChat;
    private Button buttonAppointment;
    
    private HomeModel store;
    private JsonDataManager jsonDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_home_detail);

        initViews();
        loadStoreData();
        setupListeners();
    }
    
    private void initViews() {
        buttonBack = findViewById(R.id.button_back);
        imageStore = findViewById(R.id.item_recommended_image);
        textName = findViewById(R.id.text_name);
        textLocation = findViewById(R.id.item_recommended_location);
        textRate = findViewById(R.id.rate);
        ratingBar = findViewById(R.id.rating);
        buttonPhone = findViewById(R.id.button_telepon);
        buttonChat = findViewById(R.id.button_chat);
        buttonAppointment = findViewById(R.id.button_appointment);
        
        jsonDataManager = JsonDataManager.getInstance(this);
    }
    
    private void loadStoreData() {
        // Lấy storeId từ Intent
        int storeId = getIntent().getIntExtra("STORE_ID", -1);
        
        if (storeId == -1) {
            Log.e(TAG, "No store ID provided");
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin cửa hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Load store từ JSON
        store = jsonDataManager.getStoreById(storeId);
        
        if (store == null) {
            Log.e(TAG, "Store not found with ID: " + storeId);
            Toast.makeText(this, "Lỗi: Không tìm thấy cửa hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Hiển thị dữ liệu lên UI
        displayStoreInfo();
    }
    
    private void displayStoreInfo() {
        // Set image
        if (store.getImage() != 0) {
            imageStore.setImageResource(store.getImage());
        }
        
        // Set tên
        textName.setText(store.getName());
        
        // Set địa chỉ + khoảng cách
        String locationText = store.getAddress() + " (" + store.getLocation() + ")";
        textLocation.setText(locationText);
        
        // Set rating
        ratingBar.setRating((float) store.getRating());
        textRate.setText(String.format("%.1f Stars", store.getRating()));
        
        Log.d(TAG, "Displayed store: " + store.getName());
    }
    
    private void setupListeners() {
        buttonBack.setOnClickListener(view -> onBackPressed());
        
        // Button gọi điện
        buttonPhone.setOnClickListener(view -> {
            if (store != null && store.getPhone() != null && !store.getPhone().equals("N/A")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + store.getPhone()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Số điện thoại không khả dụng", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Button chat
        buttonChat.setOnClickListener(view -> {
            Toast.makeText(this, "Chức năng chat đang được phát triển", Toast.LENGTH_SHORT).show();
        });
        
        // Button đặt lịch
        buttonAppointment.setOnClickListener(view -> {
            Toast.makeText(this, "Chức năng đặt lịch đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void hideStatusBar() {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().getDecorView().setSystemUiVisibility(3328);
            } else {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}