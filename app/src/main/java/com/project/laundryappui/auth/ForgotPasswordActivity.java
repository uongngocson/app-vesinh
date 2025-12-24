package com.project.laundryappui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.laundryappui.R;
import com.project.laundryappui.utils.InputValidator;

import java.util.Objects;

/**
 * ForgotPasswordActivity - Màn hình quên mật khẩu
 * 
 * Chức năng:
 * - Nhập email để nhận link reset password
 * - Validate email
 * - Gọi API forgot password (TODO: Backend cần implement endpoint này)
 * 
 * Note: Backend NestJS chưa có endpoint forgot-password
 * Hiện tại đang dùng demo (gửi email thành công giả lập)
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnSendResetLink;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Khởi tạo UI
        initViews();
        setupListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        btnSendResetLink = findViewById(R.id.btnSendResetLink);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        // Send button click
        btnSendResetLink.setOnClickListener(v -> handleSendResetLink());

        // Back to login click
        tvBackToLogin.setOnClickListener(v -> finish());

        // Clear error khi user nhập lại
        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilEmail.setError(null);
            }
        });
    }

    /**
     * Xử lý gửi link reset password
     */
    private void handleSendResetLink() {
        // Lấy input
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();

        // Clear error cũ
        tilEmail.setError(null);

        // Validate email
        if (!validateInput(email)) {
            return;
        }

        // Hiển thị loading
        showLoading(true);

        // Simulate API call (Backend cần implement endpoint này)
        // TODO: Thay bằng API call thực tế khi backend có endpoint
        performSendResetLink(email);
    }

    /**
     * Validate input
     */
    private boolean validateInput(String email) {
        if (InputValidator.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_empty_email));
            return false;
        } else if (!InputValidator.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }

    /**
     * Thực hiện gửi reset link
     * TODO: Tích hợp API khi backend implement
     */
    private void performSendResetLink(String email) {
        // Simulate delay
        etEmail.postDelayed(() -> {
            showLoading(false);

            // Demo: Luôn thành công
            Toast.makeText(this,
                    "Link đặt lại mật khẩu đã được gửi đến " + email + "\n" +
                    "Vui lòng kiểm tra email của bạn.",
                    Toast.LENGTH_LONG).show();

            // Quay về màn hình login sau 2s
            etEmail.postDelayed(this::finish, 2000);
        }, 1500);

        /* 
         * TODO: Khi backend có API forgot-password, thay bằng:
         * 
         * authRepository.forgotPassword(email, new AuthRepository.AuthCallback<MessageResponse>() {
         *     @Override
         *     public void onSuccess(MessageResponse data) {
         *         showLoading(false);
         *         Toast.makeText(ForgotPasswordActivity.this, 
         *             data.getMessage(), 
         *             Toast.LENGTH_LONG).show();
         *         
         *         // Quay về login
         *         etEmail.postDelayed(() -> finish(), 2000);
         *     }
         *
         *     @Override
         *     public void onError(String errorMessage) {
         *         showLoading(false);
         *         Toast.makeText(ForgotPasswordActivity.this, 
         *             errorMessage, 
         *             Toast.LENGTH_LONG).show();
         *     }
         * });
         */
    }

    /**
     * Hiển thị/ẩn loading
     */
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSendResetLink.setEnabled(!show);
        etEmail.setEnabled(!show);
    }
}

