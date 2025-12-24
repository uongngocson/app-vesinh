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
import com.project.laundryappui.api.AuthRepository;
import com.project.laundryappui.api.models.AuthResponse;
import com.project.laundryappui.utils.InputValidator;
import com.project.laundryappui.utils.SessionManager;

import java.util.Objects;

/**
 * LoginActivity - Màn hình đăng nhập
 * 
 * Chức năng:
 * - Validate input (email, password)
 * - Xử lý đăng nhập với API backend (NestJS)
 * - Lưu session và token khi đăng nhập thành công
 * - Navigate đến MainActivity sau khi đăng nhập
 * 
 * Dễ dàng mở rộng:
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
    private View loadingOverlay;
    private View cardProgress;

    // Repository & Utils
    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Khởi tạo Repository và SessionManager
        authRepository = new AuthRepository(this);
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
        loadingOverlay = findViewById(R.id.loadingOverlay);
        cardProgress = findViewById(R.id.cardProgress);
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

        // Gọi API đăng nhập
        performLogin(email, password);
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
     * Thực hiện đăng nhập với API backend
     * @param email Email
     * @param password Password
     */
    private void performLogin(String email, String password) {
        authRepository.login(email, password, new AuthRepository.AuthCallback<AuthResponse>() {
            @Override
            public void onSuccess(AuthResponse data) {
                // Đã lưu token và session trong repository
                showLoading(false);
                
                // Hiển thị thông báo
                Toast.makeText(LoginActivity.this, 
                        getString(R.string.login_success), 
                        Toast.LENGTH_SHORT).show();

                // Chuyển đến MainActivity
                navigateToMain();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Xử lý chuyển đến màn hình quên mật khẩu
     */
    private void handleForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * Xử lý chuyển đến màn hình đăng ký
     */
    private void handleSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Hiển thị/ẩn loading với overlay đẹp
     * @param show true để hiển thị, false để ẩn
     */
    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        cardProgress.setVisibility(show ? View.VISIBLE : View.GONE);
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

