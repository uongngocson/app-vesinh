package com.project.laundryappui.help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.laundryappui.R;
import com.project.laundryappui.support.SupportActivity;

/**
 * HelpActivity - Màn hình trợ giúp và hướng dẫn sử dụng ứng dụng
 *
 * Tính năng chính:
 * - Tìm kiếm trợ giúp với search bar
 * - Danh mục trợ giúp phổ biến (Getting Started, Troubleshooting, etc.)
 * - Quick help options (Video tutorials, Step guides, FAQs)
 * - Liên hệ hỗ trợ trực tiếp
 * - Professional UI với icons 24x24dp chuẩn
 *
 * Dễ dàng mở rộng:
 * - Thêm video tutorials
 * - Tích hợp search functionality
 * - Analytics tracking
 * - Offline help content
 */
public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    private MaterialToolbar toolbar;
    private FrameLayout loadingOverlay;

    // Search Components
    private TextInputLayout tilSearch;
    private TextInputEditText etSearch;

    // Category Cards
    private MaterialCardView cardGettingStarted, cardTroubleshooting, cardAccountHelp,
                           cardServicesPricing, cardBillingPayments, cardAppFeatures;

    // Help Options
    private LinearLayout layoutVideoTutorials, layoutStepGuides, layoutFaqs;

    // Contact Options
    private LinearLayout btnCallSupport, btnEmailSupport, btnLiveChat;

    // Constants
    private static final String SUPPORT_PHONE = "1900123456";
    private static final String SUPPORT_EMAIL = "support@laundryapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initializeViews();
        setupToolbar();
        setupClickListeners();
        setupSearchFunctionality();
    }

    /**
     * Khởi tạo các view components
     */
    private void initializeViews() {
        // Toolbar & Loading
        toolbar = findViewById(R.id.toolbar);
        loadingOverlay = findViewById(R.id.loadingOverlay);

        // Search Components
        tilSearch = findViewById(R.id.tilSearch);
        etSearch = findViewById(R.id.etSearch);

        // Category Cards
        cardGettingStarted = findViewById(R.id.cardGettingStarted);
        cardTroubleshooting = findViewById(R.id.cardTroubleshooting);
        cardAccountHelp = findViewById(R.id.cardAccountHelp);
        cardServicesPricing = findViewById(R.id.cardServicesPricing);
        cardBillingPayments = findViewById(R.id.cardBillingPayments);
        cardAppFeatures = findViewById(R.id.cardAppFeatures);

        // Help Options
        layoutVideoTutorials = findViewById(R.id.layoutVideoTutorials);
        layoutStepGuides = findViewById(R.id.layoutStepGuides);
        layoutFaqs = findViewById(R.id.layoutFaqs);

        // Contact Options
        btnCallSupport = findViewById(R.id.btnCallSupport);
        btnEmailSupport = findViewById(R.id.btnEmailSupport);
        btnLiveChat = findViewById(R.id.btnLiveChat);
    }

    /**
     * Thiết lập toolbar với navigation
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * Thiết lập click listeners
     */
    private void setupClickListeners() {
        // Category Cards
        cardGettingStarted.setOnClickListener(this);
        cardTroubleshooting.setOnClickListener(this);
        cardAccountHelp.setOnClickListener(this);
        cardServicesPricing.setOnClickListener(this);
        cardBillingPayments.setOnClickListener(this);
        cardAppFeatures.setOnClickListener(this);

        // Help Options
        layoutVideoTutorials.setOnClickListener(this);
        layoutStepGuides.setOnClickListener(this);
        layoutFaqs.setOnClickListener(this);

        // Contact Options
        btnCallSupport.setOnClickListener(this);
        btnEmailSupport.setOnClickListener(this);
        btnLiveChat.setOnClickListener(this);
    }

    /**
     * Thiết lập search functionality
     */
    private void setupSearchFunctionality() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            }
            return true;
        });

        // Clear focus when search loses focus
        etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                tilSearch.clearFocus();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Category Cards
        if (id == R.id.cardGettingStarted) {
            openHelpCategory("getting_started");
        } else if (id == R.id.cardTroubleshooting) {
            openHelpCategory("troubleshooting");
        } else if (id == R.id.cardAccountHelp) {
            openHelpCategory("account_help");
        } else if (id == R.id.cardServicesPricing) {
            openHelpCategory("services_pricing");
        } else if (id == R.id.cardBillingPayments) {
            openHelpCategory("billing_payments");
        } else if (id == R.id.cardAppFeatures) {
            openHelpCategory("app_features");

        // Help Options
        } else if (id == R.id.layoutVideoTutorials) {
            openVideoTutorials();
        } else if (id == R.id.layoutStepGuides) {
            openStepByStepGuides();
        } else if (id == R.id.layoutFaqs) {
            openFaqs();

        // Contact Options
        } else if (id == R.id.btnCallSupport) {
            handleCallSupport();
        } else if (id == R.id.btnEmailSupport) {
            handleEmailSupport();
        } else if (id == R.id.btnLiveChat) {
            handleLiveChat();
        }
    }

    /**
     * Mở danh mục trợ giúp
     */
    private void openHelpCategory(String category) {
        String message = "Mở trợ giúp: " + category.replace("_", " ");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Track analytics
        trackHelpCategoryView(category);

        // Trong thực tế, sẽ mở HelpCategoryActivity hoặc fragment
        // Intent intent = new Intent(this, HelpCategoryActivity.class);
        // intent.putExtra("category", category);
        // startActivity(intent);
    }

    /**
     * Mở video tutorials
     */
    private void openVideoTutorials() {
        Toast.makeText(this, "Mở video hướng dẫn", Toast.LENGTH_SHORT).show();
        trackHelpAction("video_tutorials");

        // Trong thực tế, sẽ mở VideoTutorialsActivity
        // Intent intent = new Intent(this, VideoTutorialsActivity.class);
        // startActivity(intent);
    }

    /**
     * Mở step-by-step guides
     */
    private void openStepByStepGuides() {
        Toast.makeText(this, "Mở hướng dẫn từng bước", Toast.LENGTH_SHORT).show();
        trackHelpAction("step_guides");

        // Trong thực tế, sẽ mở StepGuidesActivity
        // Intent intent = new Intent(this, StepGuidesActivity.class);
        // startActivity(intent);
    }

    /**
     * Mở FAQs
     */
    private void openFaqs() {
        Toast.makeText(this, "Mở câu hỏi thường gặp", Toast.LENGTH_SHORT).show();
        trackHelpAction("faqs");

        // Navigate to Support screen FAQs section
        // Trong thực tế, có thể mở FaqActivity riêng hoặc scroll đến FAQ section trong Support
        Intent intent = new Intent(this, SupportActivity.class);
        intent.putExtra("open_faqs", true);
        startActivity(intent);
    }

    /**
     * Xử lý gọi điện hỗ trợ
     */
    private void handleCallSupport() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
        startActivity(intent);

        trackHelpAction("call_support");
    }

    /**
     * Xử lý gửi email hỗ trợ
     */
    private void handleEmailSupport() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.help) + " - " + getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.help_subtitle));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Fallback: copy email to clipboard
            copyToClipboard(SUPPORT_EMAIL);
            Toast.makeText(this, "Đã sao chép email: " + SUPPORT_EMAIL, Toast.LENGTH_SHORT).show();
        }

        trackHelpAction("email_support");
    }

    /**
     * Xử lý chat trực tiếp
     */
    private void handleLiveChat() {
        Toast.makeText(this, getString(R.string.chat_started), Toast.LENGTH_SHORT).show();
        trackHelpAction("live_chat");

        // Trong thực tế, sẽ mở ChatActivity hoặc integrate với third-party chat SDK
        // openChatInterface();
    }

    /**
     * Thực hiện tìm kiếm
     */
    private void performSearch(String query) {
        Toast.makeText(this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
        trackHelpAction("search");

        // Trong thực tế, sẽ thực hiện search trong help database
        // và hiển thị kết quả
    }

    /**
     * Copy text to clipboard
     */
    private void copyToClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Track help actions for analytics
     */
    private void trackHelpAction(String action) {
        // Implement analytics tracking
        // FirebaseAnalytics.getInstance(this).logEvent("help_action", bundle);
    }

    /**
     * Track help category views
     */
    private void trackHelpCategoryView(String category) {
        // Implement analytics tracking
        // FirebaseAnalytics.getInstance(this).logEvent("help_category_view", bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) { // PERMISSION_REQUEST_CALL_PHONE
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleCallSupport();
            } else {
                Toast.makeText(this, "Cần quyền gọi điện để liên hệ hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
