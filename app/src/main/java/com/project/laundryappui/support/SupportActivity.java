package com.project.laundryappui.support;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.project.laundryappui.R;

/**
 * SupportActivity - Màn hình hỗ trợ khách hàng
 *
 * Chức năng chính:
 * - Quick Actions: Gọi điện, Email, Chat trực tiếp, Gửi ticket
 * - Thông tin liên hệ
 * - FAQ với các câu hỏi thường gặp
 * - Danh mục hỗ trợ với chip selection
 * - Tích hợp với hệ thống chat và ticket
 *
 * Tính năng nâng cao:
 * - Kiểm tra trạng thái chat online/offline
 * - Deep linking cho các action
 * - Analytics tracking cho support interactions
 * - Push notification cho ticket updates
 */
public class SupportActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    private MaterialToolbar toolbar;
    private FrameLayout loadingOverlay;

    // Quick Actions Cards
    private MaterialCardView cardCallSupport, cardEmailSupport, cardLiveChat, cardSubmitTicket;

    // FAQ Items
    private MaterialCardView faqItem1, faqItem2, faqItem3, faqItem4;
    private MaterialButton btnViewAllFaq;

    // Support Categories
    private ChipGroup chipGroupCategories;
    private Chip chipGeneral, chipAccount, chipService, chipPayment;
    private TextView tvCategoryDescription, tvChatStatus;

    // Constants
    private static final String SUPPORT_PHONE = "1900123456";
    private static final String SUPPORT_EMAIL = "support@laundryapp.com";
    private static final int PERMISSION_REQUEST_CALL_PHONE = 100;

    /**
     * Inner class chứa dữ liệu FAQ
     */
    private static class FaqData {
        String id;
        String title;
        String question;
        String answer;
        String category;
        int iconResId;

        FaqData(String id, String title, String question, String answer, String category, int iconResId) {
            this.id = id;
            this.title = title;
            this.question = question;
            this.answer = answer;
            this.category = category;
            this.iconResId = iconResId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        initializeViews();
        setupToolbar();
        setupClickListeners();
        setupChipGroup();
        updateChatStatus();
    }

    /**
     * Khởi tạo các view components
     */
    private void initializeViews() {
        // Toolbar & Loading
        toolbar = findViewById(R.id.toolbar);
        loadingOverlay = findViewById(R.id.loadingOverlay);

        // Quick Actions
        cardCallSupport = findViewById(R.id.cardCallSupport);
        cardEmailSupport = findViewById(R.id.cardEmailSupport);
        cardLiveChat = findViewById(R.id.cardLiveChat);
        cardSubmitTicket = findViewById(R.id.cardSubmitTicket);
        tvChatStatus = findViewById(R.id.tvChatStatus);

        // FAQ Items
        faqItem1 = findViewById(R.id.faqItem1);
        faqItem2 = findViewById(R.id.faqItem2);
        faqItem3 = findViewById(R.id.faqItem3);
        faqItem4 = findViewById(R.id.faqItem4);
        btnViewAllFaq = findViewById(R.id.btnViewAllFaq);

        // Support Categories
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        chipGeneral = findViewById(R.id.chipGeneral);
        chipAccount = findViewById(R.id.chipAccount);
        chipService = findViewById(R.id.chipService);
        chipPayment = findViewById(R.id.chipPayment);
        tvCategoryDescription = findViewById(R.id.tvCategoryDescription);
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
     * Thiết lập các click listeners
     */
    private void setupClickListeners() {
        cardCallSupport.setOnClickListener(this);
        cardEmailSupport.setOnClickListener(this);
        cardLiveChat.setOnClickListener(this);
        cardSubmitTicket.setOnClickListener(this);

        faqItem1.setOnClickListener(this);
        faqItem2.setOnClickListener(this);
        faqItem3.setOnClickListener(this);
        faqItem4.setOnClickListener(this);
        btnViewAllFaq.setOnClickListener(this);
    }

    /**
     * Thiết lập chip group cho categories
     */
    private void setupChipGroup() {
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                Chip selectedChip = findViewById(checkedId);
                updateCategoryDescription(selectedChip.getText().toString());
            } else {
                updateCategoryDescription(getString(R.string.general_questions));
            }
        });
    }

    /**
     * Cập nhật mô tả category
     */
    private void updateCategoryDescription(String category) {
        String description = getCategoryDescription(category);
        tvCategoryDescription.setText(description);
    }

    /**
     * Lấy mô tả cho từng category
     */
    private String getCategoryDescription(String category) {
        if (category.equals(getString(R.string.general_questions))) {
            return "Câu hỏi chung về ứng dụng và dịch vụ giặt ủi";
        } else if (category.equals(getString(R.string.account_support))) {
            return "Hỗ trợ đăng nhập, đăng ký và quản lý tài khoản";
        } else if (category.equals(getString(R.string.service_issues))) {
            return "Vấn đề về chất lượng dịch vụ và đơn hàng";
        } else if (category.equals(getString(R.string.payment_billing))) {
            return "Thanh toán, hóa đơn và hoàn tiền";
        }
        return "Chọn danh mục hỗ trợ để được hướng dẫn chi tiết hơn";
    }

    /**
     * Cập nhật trạng thái chat (online/offline)
     */
    private void updateChatStatus() {
        // Giả lập kiểm tra trạng thái chat
        // Trong thực tế, sẽ gọi API để check
        boolean isChatOnline = isChatServiceAvailable();
        tvChatStatus.setText(isChatOnline ? R.string.chat_online : R.string.chat_offline);
        tvChatStatus.setTextColor(getColor(isChatOnline ? android.R.color.holo_green_dark : android.R.color.darker_gray));
        cardLiveChat.setEnabled(isChatOnline);
    }

    /**
     * Kiểm tra trạng thái chat service
     */
    private boolean isChatServiceAvailable() {
        // Trong thực tế, implement logic kiểm tra server status
        // Có thể dùng Firebase Realtime Database hoặc API call
        return true; // Giả lập luôn online
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.cardCallSupport) {
            handleCallSupport();
        } else if (id == R.id.cardEmailSupport) {
            handleEmailSupport();
        } else if (id == R.id.cardLiveChat) {
            handleLiveChat();
        } else if (id == R.id.cardSubmitTicket) {
            handleSubmitTicket();
        } else if (id == R.id.faqItem1) {
            showFaqDetail(R.string.faq_how_to_order);
        } else if (id == R.id.faqItem2) {
            showFaqDetail(R.string.faq_payment_methods);
        } else if (id == R.id.faqItem3) {
            showFaqDetail(R.string.faq_delivery_time);
        } else if (id == R.id.faqItem4) {
            showFaqDetail(R.string.faq_refund_policy);
        } else if (id == R.id.btnViewAllFaq) {
            openFullFaqList();
        }
    }

    /**
     * Xử lý gọi điện hỗ trợ
     */
    public void handleCallSupport() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
        startActivity(intent);

        // Track analytics
        trackSupportAction("call_support");
    }

    /**
     * Xử lý gửi email hỗ trợ
     */
    private void handleEmailSupport() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support) + " - " + getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_subtitle));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Fallback: copy email to clipboard
            copyToClipboard(SUPPORT_EMAIL);
            Toast.makeText(this, "Đã sao chép email: " + SUPPORT_EMAIL, Toast.LENGTH_SHORT).show();
        }

        trackSupportAction("email_support");
    }

    /**
     * Xử lý chat trực tiếp
     */
    private void handleLiveChat() {
        if (!isChatServiceAvailable()) {
            Toast.makeText(this, R.string.chat_unavailable, Toast.LENGTH_SHORT).show();
            return;
        }

        // Mở chat interface
        Toast.makeText(this, R.string.chat_started, Toast.LENGTH_SHORT).show();

        // Trong thực tế, sẽ mở ChatActivity hoặc integrate với third-party chat SDK
        // openChatInterface();

        trackSupportAction("live_chat");
    }

    /**
     * Xử lý gửi ticket hỗ trợ
     */
    private void handleSubmitTicket() {
        // Mở form tạo ticket
        openTicketForm();

        trackSupportAction("submit_ticket");
    }

    /**
     * Hiển thị chi tiết FAQ
     */
    private void showFaqDetail(int questionResId) {
        String faqId = getFaqIdFromResId(questionResId);
        showFaqDetail(faqId);
    }

    /**
     * Hiển thị chi tiết FAQ theo ID
     */
    public void showFaqDetail(String faqId) {
        FaqData faqData = getFaqData(faqId);
        if (faqData != null) {
            FaqDetailDialogFragment dialog = FaqDetailDialogFragment.newInstance(
                    faqData.id,
                    faqData.title,
                    faqData.question,
                    faqData.answer,
                    faqData.category,
                    faqData.iconResId
            );
            dialog.show(getSupportFragmentManager(), "FaqDetailDialog");
            trackSupportAction("view_faq");
        }
    }

    /**
     * Lấy FAQ ID từ resource ID
     */
    private String getFaqIdFromResId(int resId) {
        if (resId == R.string.faq_how_to_order) {
            return "faq_order";
        } else if (resId == R.string.faq_payment_methods) {
            return "faq_payment";
        } else if (resId == R.string.faq_delivery_time) {
            return "faq_delivery";
        } else if (resId == R.string.faq_refund_policy) {
            return "faq_refund";
        }
        return "faq_general";
    }

    /**
     * Lấy dữ liệu FAQ chi tiết
     */
    private FaqData getFaqData(String faqId) {
        switch (faqId) {
            case "faq_order":
                return new FaqData(
                        "faq_order",
                        "Đặt đơn hàng",
                        getString(R.string.faq_how_to_order),
                        "Để đặt đơn hàng trên ứng dụng Laundry App, bạn hãy làm theo các bước sau:\n\n" +
                        "1. **Mở ứng dụng** và đăng nhập vào tài khoản của bạn\n" +
                        "2. **Chọn dịch vụ** bạn cần (Giặt ủi, Giặt khô, Ủi đồ, v.v.)\n" +
                        "3. **Chọn thời gian** lấy/giao đồ phù hợp\n" +
                        "4. **Nhập địa chỉ** giao nhận\n" +
                        "5. **Chọn phương thức thanh toán** và xác nhận đơn hàng\n" +
                        "6. **Theo dõi đơn hàng** qua mục \"Đơn hàng của tôi\"\n\n" +
                        "Đơn hàng sẽ được xử lý trong vòng 24 giờ và thông báo qua SMS.",
                        "Hướng dẫn sử dụng",
                        R.drawable.ic_box
                );

            case "faq_payment":
                return new FaqData(
                        "faq_payment",
                        "Thanh toán",
                        getString(R.string.faq_payment_methods),
                        "Laundry App hỗ trợ nhiều phương thức thanh toán an toàn:\n\n" +
                        "💳 **Thẻ tín dụng/ghi nợ**: Visa, Mastercard, JCB\n" +
                        "📱 **Ví điện tử**: Momo, ZaloPay, ViettelPay, MobiFone\n" +
                        "💵 **Tiền mặt**: Thanh toán khi nhận hàng\n" +
                        "🏦 **Chuyển khoản**: Ngân hàng trực tuyến\n\n" +
                        "Tất cả giao dịch đều được bảo mật với công nghệ mã hóa SSL 256-bit. " +
                        "Chúng tôi không lưu trữ thông tin thẻ tín dụng của bạn.",
                        "Thanh toán",
                        R.drawable.ic_wallet
                );

            case "faq_delivery":
                return new FaqData(
                        "faq_delivery",
                        "Giao nhận",
                        getString(R.string.faq_delivery_time),
                        "Thời gian giao nhận đồ giặt phụ thuộc vào dịch vụ bạn chọn:\n\n" +
                        "🚚 **Dịch vụ tiêu chuẩn**: 1-2 ngày làm việc\n" +
                        "⚡ **Dịch vụ express**: 4-6 giờ trong nội thành\n" +
                        "🏠 **Dịch vụ tận nhà**: Miễn phí trong bán kính 5km\n\n" +
                        "Bạn sẽ nhận được thông báo SMS với mã tracking để theo dõi đơn hàng. " +
                        "Nhân viên giao nhận sẽ liên hệ trước 30 phút khi đến nơi.",
                        "Giao nhận",
                        R.drawable.ic_location
                );

            case "faq_refund":
                return new FaqData(
                        "faq_refund",
                        "Hoàn tiền",
                        getString(R.string.faq_refund_policy),
                        "Chính sách hoàn tiền của Laundry App:\n\n" +
                        "✅ **Hoàn tiền 100%** trong 7 ngày nếu:\n" +
                        "   - Dịch vụ không đạt chất lượng cam kết\n" +
                        "   - Đồ bị hỏng do lỗi của chúng tôi\n" +
                        "   - Giao hàng trễ quá 24 giờ\n\n" +
                        "⏰ **Thời gian xử lý**: 3-5 ngày làm việc\n" +
                        "💰 **Phương thức hoàn tiền**: Tương ứng với phương thức thanh toán ban đầu\n\n" +
                        "Liên hệ hotline 1900 XXX XXX để được hỗ trợ hoàn tiền.",
                        "Chính sách",
                        R.drawable.ic_wallet
                );

            default:
                return new FaqData(
                        "faq_general",
                        "Câu hỏi khác",
                        "Câu hỏi của bạn",
                        "Chúng tôi sẽ cập nhật câu trả lời chi tiết trong thời gian sớm nhất. " +
                        "Vui lòng liên hệ hotline 1900 XXX XXX để được hỗ trợ trực tiếp.",
                        "Tổng hợp",
                        R.drawable.ic_help
                );
        }
    }

    /**
     * Mở danh sách FAQ đầy đủ
     */
    private void openFullFaqList() {
        // Trong thực tế, sẽ mở FaqListActivity
        Toast.makeText(this, "Mở danh sách FAQ đầy đủ", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mở form tạo ticket
     */
    private void openTicketForm() {
        // Trong thực tế, sẽ mở TicketFormActivity
        Toast.makeText(this, "Mở form tạo ticket hỗ trợ", Toast.LENGTH_SHORT).show();
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
     * Track support actions for analytics
     */
    private void trackSupportAction(String action) {
        // Implement analytics tracking
        // FirebaseAnalytics.getInstance(this).logEvent("support_action", bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleCallSupport();
            } else {
                Toast.makeText(this, "Cần quyền gọi điện để liên hệ hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
