package com.project.laundryappui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.project.laundryappui.MainActivity;
import com.project.laundryappui.R;
import com.project.laundryappui.utils.InputValidator;
import com.project.laundryappui.utils.SessionManager;

import java.util.Objects;

/**
 * LoginActivity - Màn hình đăng nhập
 * 
 * Chức năng:
 * - Validate input (email, password)
 * - Xử lý đăng nhập (Demo: email = demo@laundry.com, password = 123456)
 * - Lưu session khi đăng nhập thành công
 * - Navigate đến MainActivity sau khi đăng nhập
 * 
 * Dễ dàng mở rộng:
 * - Tích hợp API đăng nhập thật
 * - Thêm đăng nhập bằng Google, Facebook
 * - Thêm chức năng Remember Me
 * - Thêm Biometric Authentication
 */
public class LoginActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword, tvSignUp;
    private ProgressBar progressBar;

    // Utils
    private SessionManager sessionManager;

    // Demo Account (Thay bằng API call thực tế khi production)
    private static final String DEMO_EMAIL = "demo@laundry.com";
    private static final String DEMO_PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);
        
        // Kiểm tra nếu đã đăng nhập thì chuyển thẳng đến MainActivity
        if (sessionManager.isLoggedIn()) {
            navigateToMain();
            return;
        }
        
        setContentView(R.layout.activity_login);
        
        // Khởi tạo UI
        initViews();
        setupListeners();
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        // Login button click
        btnLogin.setOnClickListener(v -> handleLogin());

        // Forgot password click
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());

        // Sign up click
        tvSignUp.setOnClickListener(v -> handleSignUp());

        // Clear error khi user nhập lại
        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilEmail.setError(null);
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilPassword.setError(null);
            }
        });
    }

    /**
     * Xử lý đăng nhập
     */
    private void handleLogin() {
        // Lấy input
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString();

        // Clear errors cũ
        tilEmail.setError(null);
        tilPassword.setError(null);

        // Validate input
        if (!validateInput(email, password)) {
            return;
        }

        // Hiển thị loading
        showLoading(true);

        // Simulate API call với delay (Thay bằng API call thực tế)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            performLogin(email, password);
        }, 1500);
    }

    /**
     * Validate input từ người dùng
     * @param email Email nhập vào
     * @param password Password nhập vào
     * @return true nếu hợp lệ, false nếu không
     */
    private boolean validateInput(String email, String password) {
        boolean isValid = true;

        // Validate email
        if (InputValidator.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_empty_email));
            isValid = false;
        } else if (!InputValidator.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Validate password
        if (InputValidator.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_empty_password));
            isValid = false;
        } else if (!InputValidator.isValidPassword(password)) {
            tilPassword.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }

        return isValid;
    }

    /**
     * Thực hiện đăng nhập
     * TODO: Thay bằng API call thực tế khi production
     * @param email Email
     * @param password Password
     */
    private void performLogin(String email, String password) {
        // Demo: Kiểm tra với account demo
        // Thay bằng API call thực tế: apiService.login(email, password)
        if (email.equals(DEMO_EMAIL) && password.equals(DEMO_PASSWORD)) {
            // Đăng nhập thành công
            onLoginSuccess(email, "Demo User");
        } else {
            // Đăng nhập thất bại
            onLoginFailed();
        }
    }

    /**
     * Callback khi đăng nhập thành công
     * @param email Email người dùng
     * @param userName Tên người dùng
     */
    private void onLoginSuccess(String email, String userName) {
        showLoading(false);

        // Lưu session
        sessionManager.createLoginSession(email, userName);

        // Hiển thị thông báo
        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();

        // Chuyển đến MainActivity
        navigateToMain();
    }

    /**
     * Callback khi đăng nhập thất bại
     */
    private void onLoginFailed() {
        showLoading(false);
        Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
    }

    /**
     * Xử lý quên mật khẩu
     * TODO: Implement màn hình forgot password
     */
    private void handleForgotPassword() {
        Toast.makeText(this, "Tính năng đang được phát triển", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, ForgotPasswordActivity.class);
        // startActivity(intent);
    }

    /**
     * Xử lý đăng ký
     * TODO: Implement màn hình sign up
     */
    private void handleSignUp() {
        Toast.makeText(this, "Tính năng đang được phát triển", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, SignUpActivity.class);
        // startActivity(intent);
    }

    /**
     * Hiển thị/ẩn loading
     * @param show true để hiển thị, false để ẩn
     */
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        etEmail.setEnabled(!show);
        etPassword.setEnabled(!show);
    }

    /**
     * Navigate đến MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Override onBackPressed để thoát app thay vì quay lại
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Đóng tất cả activities và thoát app
    }
}

