package com.project.laundryappui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.laundryappui.R;
import com.project.laundryappui.api.AuthRepository;
import com.project.laundryappui.utils.InputValidator;

import java.util.Objects;

/**
 * ChangePasswordActivity - Màn hình đổi mật khẩu
 * 
 * Chức năng:
 * - Validate input (current password, new password, confirm password)
 * - Gọi API đổi mật khẩu với backend NestJS
 * - Tự động logout và yêu cầu đăng nhập lại sau khi thành công
 */
public class ChangePasswordActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmPassword;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private MaterialButton btnChangePassword;
    private ProgressBar progressBar;
    private View loadingOverlay;
    private View cardProgress;

    // Repository
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo Repository
        authRepository = new AuthRepository(this);

        // Khởi tạo UI
        initViews();
        setupToolbar();
        setupListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tilCurrentPassword = findViewById(R.id.tilCurrentPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        
        btnChangePassword = findViewById(R.id.btnChangePassword);
        progressBar = findViewById(R.id.progressBar);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        cardProgress = findViewById(R.id.cardProgress);
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        // Change Password button click
        btnChangePassword.setOnClickListener(v -> handleChangePassword());

        // Clear error khi user nhập lại
        etCurrentPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilCurrentPassword.setError(null);
        });

        etNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilNewPassword.setError(null);
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilConfirmPassword.setError(null);
        });
    }

    /**
     * Xử lý đổi mật khẩu
     */
    private void handleChangePassword() {
        // Lấy input
        String currentPassword = Objects.requireNonNull(etCurrentPassword.getText()).toString();
        String newPassword = Objects.requireNonNull(etNewPassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(etConfirmPassword.getText()).toString();

        // Clear errors cũ
        clearErrors();

        // Validate input
        if (!validateInput(currentPassword, newPassword, confirmPassword)) {
            return;
        }

        // Hiển thị loading
        showLoading(true);

        // Gọi API đổi mật khẩu
        performChangePassword(currentPassword, newPassword, confirmPassword);
    }

    /**
     * Validate input từ người dùng
     */
    private boolean validateInput(String currentPassword, String newPassword, String confirmPassword) {
        boolean isValid = true;

        // Validate current password
        if (InputValidator.isEmpty(currentPassword)) {
            tilCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            isValid = false;
        }

        // Validate new password với rule mạnh
        if (InputValidator.isEmpty(newPassword)) {
            tilNewPassword.setError("Vui lòng nhập mật khẩu mới");
            isValid = false;
        } else if (!InputValidator.isStrongPassword(newPassword)) {
            tilNewPassword.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số");
            isValid = false;
        }

        // Validate confirm password
        if (InputValidator.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            isValid = false;
        }

        // Check nếu mật khẩu mới giống mật khẩu cũ
        if (!InputValidator.isEmpty(currentPassword) && 
            !InputValidator.isEmpty(newPassword) && 
            currentPassword.equals(newPassword)) {
            tilNewPassword.setError("Mật khẩu mới phải khác mật khẩu hiện tại");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Thực hiện đổi mật khẩu với API backend
     */
    private void performChangePassword(String currentPassword, String newPassword, String confirmPassword) {
        authRepository.changePassword(currentPassword, newPassword, confirmPassword,
                new AuthRepository.AuthCallback<String>() {
            @Override
            public void onSuccess(String message) {
                showLoading(false);
                
                // Hiển thị thông báo
                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();

                // Backend đã logout tất cả sessions, chuyển về màn hình đăng nhập
                navigateToLogin();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(ChangePasswordActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Navigate đến LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
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
        btnChangePassword.setEnabled(!show);
        etCurrentPassword.setEnabled(!show);
        etNewPassword.setEnabled(!show);
        etConfirmPassword.setEnabled(!show);
    }

    /**
     * Clear tất cả errors
     */
    private void clearErrors() {
        tilCurrentPassword.setError(null);
        tilNewPassword.setError(null);
        tilConfirmPassword.setError(null);
    }
}

