package com.project.laundryappui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.laundryappui.api.OrdersRepository;
import com.project.laundryappui.api.models.Address;
import com.project.laundryappui.api.models.CleaningBookingRequest;
import com.project.laundryappui.api.models.Order;
import com.project.laundryappui.utils.InputValidator;

/**
 * ShoeBookingActivity - Màn hình đặt đơn vệ sinh giày/túi xách
 *
 * Chức năng:
 * - Nhập thông tin khách hàng và sản phẩm
 * - Validate dữ liệu đầu vào
 * - Gọi API tạo đơn hàng vệ sinh
 * - Hiển thị kết quả và điều hướng
 *
 * UI Features:
 * - Material Design components
 * - Loading states
 * - Input validation
 * - Error handling
 */
public class ShoeBookingActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout tilCustomerName, tilCustomerPhone, tilCustomerEmail,
                           tilItemDescription, tilServiceNotes, tilNotes,
                           tilShippingStreet, tilShippingDistrict, tilShippingCity,
                           tilBillingStreet, tilBillingDistrict, tilBillingCity;
    private TextInputEditText etCustomerName, etCustomerPhone, etCustomerEmail,
                             etItemDescription, etServiceNotes, etNotes,
                             etShippingStreet, etShippingDistrict, etShippingCity,
                             etBillingStreet, etBillingDistrict, etBillingCity;
    private MaterialButton btnSubmit;
    private View loadingOverlay;
    private View cardProgress;
    private ProgressBar progressBar;
    private TextView tvProgressMessage;

    // Repository & Utils
    private OrdersRepository ordersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoe_booking);

        // Khởi tạo Repository
        ordersRepository = new OrdersRepository(this);

        // Khởi tạo UI
        initViews();
        setupListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        // Text Input Layouts
        tilCustomerName = findViewById(R.id.tilCustomerName);
        tilCustomerPhone = findViewById(R.id.tilCustomerPhone);
        tilCustomerEmail = findViewById(R.id.tilCustomerEmail);
        tilItemDescription = findViewById(R.id.tilItemDescription);
        tilServiceNotes = findViewById(R.id.tilServiceNotes);
        tilNotes = findViewById(R.id.tilNotes);

        // Shipping Address Layouts
        tilShippingStreet = findViewById(R.id.tilShippingStreet);
        tilShippingDistrict = findViewById(R.id.tilShippingDistrict);
        tilShippingCity = findViewById(R.id.tilShippingCity);

        // Billing Address Layouts
        tilBillingStreet = findViewById(R.id.tilBillingStreet);
        tilBillingDistrict = findViewById(R.id.tilBillingDistrict);
        tilBillingCity = findViewById(R.id.tilBillingCity);

        // Text Input EditTexts
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etCustomerEmail = findViewById(R.id.etCustomerEmail);
        etItemDescription = findViewById(R.id.etItemDescription);
        etServiceNotes = findViewById(R.id.etServiceNotes);
        etNotes = findViewById(R.id.etNotes);

        // Shipping Address EditTexts
        etShippingStreet = findViewById(R.id.etShippingStreet);
        etShippingDistrict = findViewById(R.id.etShippingDistrict);
        etShippingCity = findViewById(R.id.etShippingCity);

        // Billing Address EditTexts
        etBillingStreet = findViewById(R.id.etBillingStreet);
        etBillingDistrict = findViewById(R.id.etBillingDistrict);
        etBillingCity = findViewById(R.id.etBillingCity);

        // Buttons
        btnSubmit = findViewById(R.id.btnSubmit);

        // Loading components
        loadingOverlay = findViewById(R.id.loadingOverlay);
        cardProgress = findViewById(R.id.cardProgress);
        progressBar = findViewById(R.id.progressBar);
        tvProgressMessage = findViewById(R.id.tvProgressMessage);
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        // Submit button click
        btnSubmit.setOnClickListener(v -> handleSubmit());

        // Back button click
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Clear error khi user nhập lại
        setupErrorClearListeners();
    }

    /**
     * Setup listeners để clear error khi user nhập lại
     */
    private void setupErrorClearListeners() {
        etCustomerName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilCustomerName.setError(null);
        });

        etCustomerPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilCustomerPhone.setError(null);
        });

        etCustomerEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilCustomerEmail.setError(null);
        });

        etItemDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilItemDescription.setError(null);
        });

        // Shipping Address listeners
        etShippingStreet.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilShippingStreet.setError(null);
        });

        etShippingDistrict.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilShippingDistrict.setError(null);
        });

        etShippingCity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilShippingCity.setError(null);
        });
    }

    /**
     * Xử lý submit form
     */
    private void handleSubmit() {
        // Lấy dữ liệu từ form
        String customerName = etCustomerName.getText().toString().trim();
        String customerPhone = etCustomerPhone.getText().toString().trim();
        String customerEmail = etCustomerEmail.getText().toString().trim();
        String itemDescription = etItemDescription.getText().toString().trim();
        String serviceNotes = etServiceNotes.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        // Lấy địa chỉ shipping
        String shippingStreet = etShippingStreet.getText().toString().trim();
        String shippingDistrict = etShippingDistrict.getText().toString().trim();
        String shippingCity = etShippingCity.getText().toString().trim();

        // Clear errors cũ
        clearAllErrors();

        // Validate dữ liệu
        if (!validateForm(customerName, customerPhone, customerEmail, itemDescription,
                         shippingStreet, shippingDistrict, shippingCity)) {
            return;
        }

        // Tạo request object
        CleaningBookingRequest request = createCleaningBookingRequest(
            customerName, customerPhone, customerEmail,
            itemDescription, serviceNotes, notes,
            shippingStreet, shippingDistrict, shippingCity
        );

        // Gọi API
        submitCleaningBooking(request);
    }

    /**
     * Validate toàn bộ form
     */
    private boolean validateForm(String customerName, String customerPhone,
                               String customerEmail, String itemDescription,
                               String shippingStreet, String shippingDistrict, String shippingCity) {
        boolean isValid = true;

        // Validate customer name
        if (InputValidator.isEmpty(customerName)) {
            tilCustomerName.setError(getString(R.string.error_empty_name));
            isValid = false;
        } else if (customerName.length() < 2) {
            tilCustomerName.setError("Tên phải có ít nhất 2 ký tự");
            isValid = false;
        }

        // Validate customer phone
        if (InputValidator.isEmpty(customerPhone)) {
            tilCustomerPhone.setError(getString(R.string.error_empty_phone));
            isValid = false;
        } else if (!InputValidator.isValidPhone(customerPhone)) {
            tilCustomerPhone.setError(getString(R.string.error_invalid_phone));
            isValid = false;
        }

        // Validate customer email (optional)
        if (!InputValidator.isEmpty(customerEmail) && !InputValidator.isValidEmail(customerEmail)) {
            tilCustomerEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Validate item description
        if (InputValidator.isEmpty(itemDescription)) {
            tilItemDescription.setError("Vui lòng mô tả sản phẩm cần vệ sinh");
            isValid = false;
        } else if (itemDescription.length() < 5) {
            tilItemDescription.setError("Mô tả sản phẩm phải có ít nhất 5 ký tự");
            isValid = false;
        }

        // Validate shipping address
        if (InputValidator.isEmpty(shippingStreet)) {
            tilShippingStreet.setError("Vui lòng nhập số nhà, tên đường");
            isValid = false;
        }

        if (InputValidator.isEmpty(shippingDistrict)) {
            tilShippingDistrict.setError("Vui lòng nhập quận/huyện");
            isValid = false;
        }

        if (InputValidator.isEmpty(shippingCity)) {
            tilShippingCity.setError("Vui lòng nhập tỉnh/thành phố");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Clear tất cả error messages
     */
    private void clearAllErrors() {
        tilCustomerName.setError(null);
        tilCustomerPhone.setError(null);
        tilCustomerEmail.setError(null);
        tilItemDescription.setError(null);
        tilServiceNotes.setError(null);
        tilNotes.setError(null);
        tilShippingStreet.setError(null);
        tilShippingDistrict.setError(null);
        tilShippingCity.setError(null);
        tilBillingStreet.setError(null);
        tilBillingDistrict.setError(null);
        tilBillingCity.setError(null);
    }

    /**
     * Tạo CleaningBookingRequest từ dữ liệu form
     */
    private CleaningBookingRequest createCleaningBookingRequest(
            String customerName, String customerPhone, String customerEmail,
            String itemDescription, String serviceNotes, String notes,
            String shippingStreet, String shippingDistrict, String shippingCity) {

        CleaningBookingRequest request = new CleaningBookingRequest();
        request.setCustomerName(customerName);
        request.setCustomerPhone(customerPhone);

        // Email optional
        if (!InputValidator.isEmpty(customerEmail)) {
            request.setCustomerEmail(customerEmail);
        }

        // Create shipping address
        Address shippingAddress = new Address();
        shippingAddress.setAddressLine1(shippingStreet);
        shippingAddress.setDistrict(shippingDistrict);
        shippingAddress.setCity(shippingCity);
        request.setShippingAddress(shippingAddress);

        // Create billing address (optional - copy from shipping if not filled)
        String billingStreet = etBillingStreet.getText().toString().trim();
        String billingDistrict = etBillingDistrict.getText().toString().trim();
        String billingCity = etBillingCity.getText().toString().trim();

        if (!InputValidator.isEmpty(billingStreet) && !InputValidator.isEmpty(billingDistrict) && !InputValidator.isEmpty(billingCity)) {
            Address billingAddress = new Address();
            billingAddress.setAddressLine1(billingStreet);
            billingAddress.setDistrict(billingDistrict);
            billingAddress.setCity(billingCity);
            request.setBillingAddress(billingAddress);
        }

        request.setItemDescription(itemDescription);

        // Service notes optional
        if (!InputValidator.isEmpty(serviceNotes)) {
            request.setServiceNotes(serviceNotes);
        }

        // Notes optional
        if (!InputValidator.isEmpty(notes)) {
            request.setNotes(notes);
        }

        return request;
    }

    /**
     * Gọi API tạo đơn hàng vệ sinh
     */
    private void submitCleaningBooking(CleaningBookingRequest request) {
        showLoading(true, "Đang tạo đơn hàng vệ sinh...");

        ordersRepository.createCleaningBooking(request, new OrdersRepository.OrdersCallback<Order>() {
    @Override
            public void onSuccess(Order order) {
                runOnUiThread(() -> {
                    showLoading(false, null);
                    handleBookingSuccess(order);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    showLoading(false, null);
                    handleBookingError(errorMessage);
                });
            }
        });
    }

    /**
     * Xử lý khi đặt đơn thành công
     */
    private void handleBookingSuccess(Order order) {
        // Hiển thị success message với order number
        String successMessage = "Đặt đơn vệ sinh thành công!\nMã đơn: " + order.getOrderNumber();
        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();

        // Có thể navigate đến màn hình chi tiết đơn hàng hoặc về màn hình chính
        // Hiện tại sẽ finish activity và quay về màn hình chính
        finish();
    }

    /**
     * Xử lý khi đặt đơn thất bại
     */
    private void handleBookingError(String errorMessage) {
        Toast.makeText(this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Hiển thị/ẩn loading với message tùy chỉnh
     */
    private void showLoading(boolean show, String message) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        cardProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSubmit.setEnabled(!show);

        if (message != null) {
            tvProgressMessage.setText(message);
        }
    }

    /**
     * Override onBackPressed để đảm bảo loading được tắt
     */
    @Override
    public void onBackPressed() {
        if (loadingOverlay.getVisibility() == View.VISIBLE) {
            // Nếu đang loading, không cho phép back
            return;
        }
        super.onBackPressed();
    }
}