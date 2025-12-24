package com.project.laundryappui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.laundryappui.MainActivity;
import com.project.laundryappui.R;
import com.project.laundryappui.api.AuthRepository;
import com.project.laundryappui.api.models.AuthResponse;
import com.project.laundryappui.utils.InputValidator;

import java.util.Objects;

/**
 * SignUpActivity - Màn hình đăng ký tài khoản mới
 * 
 * Chức năng:
 * - Validate input (full name, email, phone, password)
 * - Gọi API đăng ký với backend NestJS
 * - Tự động đăng nhập sau khi đăng ký thành công
 */
public class SignUpActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout tilFullName, tilEmail, tilPhone, tilPassword;
    private TextInputEditText etFullName, etEmail, etPhone, etPassword;
    private MaterialButton btnSignUp;
    private TextView tvLogin;
    private ProgressBar progressBar;
    private View loadingOverlay;
    private View cardProgress;

    // Repository
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Khởi tạo Repository
        authRepository = new AuthRepository(this);

        // Khởi tạo UI
        initViews();
        setupListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilPassword = findViewById(R.id.tilPassword);
        
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        cardProgress = findViewById(R.id.cardProgress);
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        // Sign Up button click
        btnSignUp.setOnClickListener(v -> handleSignUp());

        // Login text click
        tvLogin.setOnClickListener(v -> finish());

        // Clear error khi user nhập lại
        etFullName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilFullName.setError(null);
        });

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilEmail.setError(null);
        });

        etPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilPhone.setError(null);
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilPassword.setError(null);
        });
    }

    /**
     * Xử lý đăng ký
     */
    private void handleSignUp() {
        // Lấy input
        String fullName = Objects.requireNonNull(etFullName.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String phone = Objects.requireNonNull(etPhone.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString();

        // Clear errors cũ
        clearErrors();

        // Validate input
        if (!validateInput(fullName, email, phone, password)) {
            return;
        }

        // Hiển thị loading
        showLoading(true);

        // Gọi API đăng ký
        performSignUp(fullName, email, phone, password);
    }

    /**
     * Validate input từ người dùng
     */
    private boolean validateInput(String fullName, String email, String phone, String password) {
        boolean isValid = true;

        // Validate full name
        if (InputValidator.isEmpty(fullName)) {
            tilFullName.setError("Vui lòng nhập họ tên");
            isValid = false;
        } else if (!InputValidator.isValidName(fullName)) {
            tilFullName.setError("Họ tên phải có ít nhất 2 ký tự");
            isValid = false;
        }

        // Validate email
        if (InputValidator.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_empty_email));
            isValid = false;
        } else if (!InputValidator.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Validate phone (optional nhưng nếu có thì phải đúng format)
        if (!InputValidator.isEmpty(phone) && !InputValidator.isValidPhone(phone)) {
            tilPhone.setError("Số điện thoại không hợp lệ (VD: 0912345678)");
            isValid = false;
        }

        // Validate password với rule mạnh (backend yêu cầu)
        if (InputValidator.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_empty_password));
            isValid = false;
        } else if (!InputValidator.isStrongPassword(password)) {
            tilPassword.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Thực hiện đăng ký với API backend
     */
    private void performSignUp(String fullName, String email, String phone, String password) {
        // Phone có thể null nếu user không nhập
        String phoneValue = InputValidator.isEmpty(phone) ? null : phone;
        
        authRepository.register(email, phoneValue, password, fullName, 
                new AuthRepository.AuthCallback<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse data) {
                showLoading(false);
                
                // Hiển thị thông báo
                Toast.makeText(SignUpActivity.this, 
                        "Đăng ký thành công! Chào mừng " + data.getUser().getFullName(), 
                        Toast.LENGTH_SHORT).show();

                // Tự động chuyển đến MainActivity
                navigateToMain();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Navigate đến MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Hiển thị/ẩn loading với overlay đẹp
     */
    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        cardProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSignUp.setEnabled(!show);
        etFullName.setEnabled(!show);
        etEmail.setEnabled(!show);
        etPhone.setEnabled(!show);
        etPassword.setEnabled(!show);
    }

    /**
     * Clear tất cả errors
     */
    private void clearErrors() {
        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilPassword.setError(null);
    }
}

