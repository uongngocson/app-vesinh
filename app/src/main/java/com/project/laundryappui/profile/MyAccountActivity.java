package com.project.laundryappui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.laundryappui.R;
import com.project.laundryappui.api.UsersRepository;
import com.project.laundryappui.api.models.UpdateProfileRequest;
import com.project.laundryappui.api.models.User;
import com.project.laundryappui.api.models.UserProfile;
import com.project.laundryappui.utils.InputValidator;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * MyAccountActivity - Màn hình quản lý thông tin tài khoản
 * 
 * Chức năng:
 * - Hiển thị thông tin profile từ API
 * - Chỉnh sửa và cập nhật profile (fullName, phone, gender, dateOfBirth)
 * - Hiển thị thông tin membership (tier, points, balance)
 * - Validate input trước khi gửi API
 * 
 * Dễ dàng mở rộng:
 * - Thêm chức năng upload avatar
 * - Thêm quản lý địa chỉ
 * - Thêm lịch sử giao dịch
 */
public class MyAccountActivity extends AppCompatActivity {

    // UI Components - Header
    private MaterialToolbar toolbar;
    private ImageView ivAvatar;
    private TextView tvFullName, tvEmail;
    private Chip chipRole;

    // UI Components - Profile Form
    private TextInputLayout tilFullName, tilPhone, tilGender, tilDateOfBirth;
    private TextInputEditText etFullName, etPhone, etDateOfBirth;
    private AutoCompleteTextView actvGender;
    private MaterialButton btnSaveProfile;

    // UI Components - Membership
    private MaterialCardView cardMembership;
    private TextView tvTier, tvPoints, tvBalance, tvTotalSpent;

    // UI Components - Loading
    private View loadingOverlay;
    private MaterialCardView cardProgress;
    private ProgressBar progressBar;

    // Repository
    private UsersRepository usersRepository;

    // Data
    private UserProfile currentProfile;
    private String[] genderOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_my_account);

            // Khởi tạo Repository
            usersRepository = new UsersRepository(this);

            // Khởi tạo UI
            initViews();
            setupListeners();
            setupGenderDropdown();

            // Load profile từ API
            loadProfile();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Khởi tạo các view components
     */
    private void initViews() {
        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        // Set navigation icon và handler trực tiếp, không dùng setSupportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        // Header
        ivAvatar = findViewById(R.id.ivAvatar);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        chipRole = findViewById(R.id.chipRole);

        // Profile Form
        tilFullName = findViewById(R.id.tilFullName);
        tilPhone = findViewById(R.id.tilPhone);
        tilGender = findViewById(R.id.tilGender);
        tilDateOfBirth = findViewById(R.id.tilDateOfBirth);
        
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        actvGender = findViewById(R.id.actvGender);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Membership
        cardMembership = findViewById(R.id.cardMembership);
        tvTier = findViewById(R.id.tvTier);
        tvPoints = findViewById(R.id.tvPoints);
        tvBalance = findViewById(R.id.tvBalance);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);

        // Loading
        loadingOverlay = findViewById(R.id.loadingOverlay);
        cardProgress = findViewById(R.id.cardProgress);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        // Toolbar back button
        toolbar.setNavigationOnClickListener(v -> finish());

        // Save profile button
        btnSaveProfile.setOnClickListener(v -> handleSaveProfile());

        // Date picker
        etDateOfBirth.setOnClickListener(v -> showDatePicker());

        // Clear errors khi user nhập lại
        etFullName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilFullName.setError(null);
            }
        });

        etPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilPhone.setError(null);
            }
        });
    }

    /**
     * Setup Gender dropdown
     */
    private void setupGenderDropdown() {
        genderOptions = new String[]{
                getString(R.string.male),
                getString(R.string.female),
                getString(R.string.other)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                genderOptions
        );
        actvGender.setAdapter(adapter);
    }

    /**
     * Load profile từ API
     */
    private void loadProfile() {
        showLoading(true);

        usersRepository.getProfile(new UsersRepository.UsersCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                showLoading(false);
                currentProfile = profile;
                displayProfile(profile);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(MyAccountActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Hiển thị dữ liệu profile lên UI
     */
    private void displayProfile(UserProfile profile) {
        if (profile == null) return;
        
        try {
            // Header
            if (profile.getFullName() != null) {
                tvFullName.setText(profile.getFullName());
            }
            if (profile.getEmail() != null) {
                tvEmail.setText(profile.getEmail());
            }
            
            // Role chip
            String role = profile.getRole();
            if ("admin".equals(role)) {
                chipRole.setText(R.string.admin);
            } else if ("staff".equals(role)) {
                chipRole.setText(R.string.staff);
            } else {
                chipRole.setText(R.string.customer);
            }

            // Profile Form
            etFullName.setText(profile.getFullName() != null ? profile.getFullName() : "");
            etPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
            etDateOfBirth.setText(profile.getDateOfBirth() != null ? profile.getDateOfBirth() : "");

            // Gender
            if (profile.getGender() != null) {
                String gender = profile.getGender();
                if ("male".equals(gender)) {
                    actvGender.setText(getString(R.string.male), false);
                } else if ("female".equals(gender)) {
                    actvGender.setText(getString(R.string.female), false);
                } else if ("other".equals(gender)) {
                    actvGender.setText(getString(R.string.other), false);
                }
            }
            
            // Membership
            displayMembership(profile);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi hiển thị profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Hiển thị thông tin membership
     */
    private void displayMembership(UserProfile profile) {
        if (profile == null) return;
        
        User.Membership membership = profile.getMembership();
        if (membership != null) {
            cardMembership.setVisibility(View.VISIBLE);
            
            // Tier
            String tier = membership.getCurrentTier();
            if (tier != null && !tier.isEmpty()) {
                tvTier.setText(tier.substring(0, 1).toUpperCase() + tier.substring(1));
            } else {
                tvTier.setText("Bronze");
            }
            
            // Points
            tvPoints.setText(String.valueOf(membership.getTotalPoints()));
            
            // Balance - Convert string to number then format
            try {
                String balanceStr = membership.getAccountBalance();
                if (balanceStr != null && !balanceStr.isEmpty()) {
                    double balance = Double.parseDouble(balanceStr);
                    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                    tvBalance.setText(currencyFormat.format(balance) + "đ");
                } else {
                    tvBalance.setText("0đ");
                }
            } catch (Exception e) {
                tvBalance.setText("0đ");
            }
            
            // Total Spent - Convert string to number then format
            try {
                String spentStr = membership.getTotalSpent();
                if (spentStr != null && !spentStr.isEmpty()) {
                    double spent = Double.parseDouble(spentStr);
                    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                    tvTotalSpent.setText(currencyFormat.format(spent) + "đ");
                } else {
                    tvTotalSpent.setText("0đ");
                }
            } catch (Exception e) {
                tvTotalSpent.setText("0đ");
            }
        } else {
            cardMembership.setVisibility(View.GONE);
        }
    }
    
    /**
     * Xử lý lưu profile
     */
    private void handleSaveProfile() {
        // Lấy input
        String fullName = Objects.requireNonNull(etFullName.getText()).toString().trim();
        String phone = Objects.requireNonNull(etPhone.getText()).toString().trim();
        String genderText = actvGender.getText().toString();
        String dateOfBirth = Objects.requireNonNull(etDateOfBirth.getText()).toString().trim();

        // Clear errors cũ
        tilFullName.setError(null);
        tilPhone.setError(null);

        // Validate input
        if (!validateInput(fullName, phone)) {
            return;
        }

        // Chuyển gender về English
        String gender = null;
        if (!genderText.isEmpty()) {
            if (genderText.equals(getString(R.string.male))) {
                gender = "male";
            } else if (genderText.equals(getString(R.string.female))) {
                gender = "female";
            } else if (genderText.equals(getString(R.string.other))) {
                gender = "other";
            }
        }

        // Tạo request
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName(fullName);
        request.setPhone(phone.isEmpty() ? null : phone);
        request.setGender(gender);
        request.setDateOfBirth(dateOfBirth.isEmpty() ? null : dateOfBirth);

        // Gọi API
        showLoading(true);
        usersRepository.updateProfile(request, new UsersRepository.UsersCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                showLoading(false);
                currentProfile = profile;
                displayProfile(profile);
                Toast.makeText(MyAccountActivity.this, 
                        R.string.update_profile_success, 
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(MyAccountActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Validate input từ người dùng
     */
    private boolean validateInput(String fullName, String phone) {
        boolean isValid = true;

        // Validate full name
        if (InputValidator.isEmpty(fullName)) {
            tilFullName.setError(getString(R.string.error_empty_name));
            isValid = false;
        } else if (!InputValidator.isValidName(fullName)) {
            tilFullName.setError(getString(R.string.error_invalid_name));
            isValid = false;
        }

        // Validate phone (optional)
        if (!InputValidator.isEmpty(phone) && !InputValidator.isValidPhone(phone)) {
            tilPhone.setError(getString(R.string.error_invalid_phone));
            isValid = false;
        }

        return isValid;
    }

    /**
     * Hiển thị Date Picker
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        // Parse ngày hiện tại nếu có
        String currentDate = Objects.requireNonNull(etDateOfBirth.getText()).toString();
        if (!currentDate.isEmpty()) {
            try {
                String[] parts = currentDate.split("-");
                if (parts.length == 3) {
                    calendar.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.US, "%04d-%02d-%02d", 
                            year, month + 1, dayOfMonth);
                    etDateOfBirth.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set max date to today
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Hiển thị/ẩn loading với overlay đẹp
     */
    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        cardProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSaveProfile.setEnabled(!show);
    }
}

