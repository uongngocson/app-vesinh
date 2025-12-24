package com.project.laundryappui.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.project.laundryappui.R;

/**
 * FaqDetailDialogFragment - Dialog hiển thị chi tiết FAQ
 *
 * Tính năng:
 * - Hiển thị câu hỏi và câu trả lời chi tiết
 * - Icon và category indicator
 * - Related links và additional info
 * - Action buttons (Contact support, Close)
 * - Smooth animations và transitions
 * - Responsive design
 */
public class FaqDetailDialogFragment extends DialogFragment implements View.OnClickListener {

    // Constants for arguments
    private static final String ARG_FAQ_ID = "faq_id";
    private static final String ARG_FAQ_TITLE = "faq_title";
    private static final String ARG_FAQ_QUESTION = "faq_question";
    private static final String ARG_FAQ_ANSWER = "faq_answer";
    private static final String ARG_FAQ_CATEGORY = "faq_category";
    private static final String ARG_FAQ_ICON = "faq_icon";

    // UI Components
    private ImageView ivFaqIcon, btnClose;
    private TextView tvFaqTitle, tvFaqCategory, tvFaqQuestion, tvFaqAnswer, tvAdditionalInfo;
    private LinearLayout layoutAdditionalInfo, layoutRelatedLinks;
    private MaterialButton btnContactSupport, btnCloseDialog;
    private Chip chipRelated1, chipRelated2;

    // Data
    private String faqId;
    private String faqTitle;
    private String faqQuestion;
    private String faqAnswer;
    private String faqCategory;
    private int faqIconResId;

    /**
     * Factory method để tạo instance của dialog
     */
    public static FaqDetailDialogFragment newInstance(String faqId, String title, String question,
                                                      String answer, String category, int iconResId) {
        FaqDetailDialogFragment fragment = new FaqDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FAQ_ID, faqId);
        args.putString(ARG_FAQ_TITLE, title);
        args.putString(ARG_FAQ_QUESTION, question);
        args.putString(ARG_FAQ_ANSWER, answer);
        args.putString(ARG_FAQ_CATEGORY, category);
        args.putInt(ARG_FAQ_ICON, iconResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy arguments
        if (getArguments() != null) {
            faqId = getArguments().getString(ARG_FAQ_ID);
            faqTitle = getArguments().getString(ARG_FAQ_TITLE);
            faqQuestion = getArguments().getString(ARG_FAQ_QUESTION);
            faqAnswer = getArguments().getString(ARG_FAQ_ANSWER);
            faqCategory = getArguments().getString(ARG_FAQ_CATEGORY);
            faqIconResId = getArguments().getInt(ARG_FAQ_ICON);
        }

        // Set dialog style
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_App_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_faq_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupData();
        setupClickListeners();
        setupAnimations();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set dialog width to 90% of screen width
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    /**
     * Khởi tạo các view components
     */
    private void initializeViews(View view) {
        ivFaqIcon = view.findViewById(R.id.ivFaqIcon);
        btnClose = view.findViewById(R.id.btnClose);
        tvFaqTitle = view.findViewById(R.id.tvFaqTitle);
        tvFaqCategory = view.findViewById(R.id.tvFaqCategory);
        tvFaqQuestion = view.findViewById(R.id.tvFaqQuestion);
        tvFaqAnswer = view.findViewById(R.id.tvFaqAnswer);
        tvAdditionalInfo = view.findViewById(R.id.tvAdditionalInfo);
        layoutAdditionalInfo = view.findViewById(R.id.layoutAdditionalInfo);
        layoutRelatedLinks = view.findViewById(R.id.layoutRelatedLinks);
        btnContactSupport = view.findViewById(R.id.btnContactSupport);
        btnCloseDialog = view.findViewById(R.id.btnCloseDialog);
        chipRelated1 = view.findViewById(R.id.chipRelated1);
        chipRelated2 = view.findViewById(R.id.chipRelated2);
    }

    /**
     * Thiết lập dữ liệu cho UI
     */
    private void setupData() {
        // Set basic info
        tvFaqTitle.setText(faqTitle);
        tvFaqCategory.setText(faqCategory);
        tvFaqQuestion.setText(faqQuestion);
        tvFaqAnswer.setText(faqAnswer);
        ivFaqIcon.setImageResource(faqIconResId);

        // Set additional content based on FAQ type
        setupAdditionalContent();
    }

    /**
     * Thiết lập nội dung bổ sung dựa trên loại FAQ
     */
    private void setupAdditionalContent() {
        switch (faqId) {
            case "faq_order":
                setupOrderFaq();
                break;
            case "faq_payment":
                setupPaymentFaq();
                break;
            case "faq_delivery":
                setupDeliveryFaq();
                break;
            case "faq_refund":
                setupRefundFaq();
                break;
            default:
                // Hide additional content for other FAQs
                layoutAdditionalInfo.setVisibility(View.GONE);
                layoutRelatedLinks.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Thiết lập nội dung cho FAQ đặt hàng
     */
    private void setupOrderFaq() {
        layoutAdditionalInfo.setVisibility(View.VISIBLE);
        layoutRelatedLinks.setVisibility(View.VISIBLE);

        tvAdditionalInfo.setText("Nếu bạn gặp khó khăn khi đặt hàng, hãy thử làm mới ứng dụng hoặc liên hệ hotline 1900 XXX XXX để được hỗ trợ trực tiếp.");

        chipRelated1.setText("Hướng dẫn thanh toán");
        chipRelated2.setText("Theo dõi đơn hàng");
        chipRelated1.setOnClickListener(v -> showRelatedFaq("faq_payment"));
        chipRelated2.setOnClickListener(v -> showRelatedFaq("faq_tracking"));
    }

    /**
     * Thiết lập nội dung cho FAQ thanh toán
     */
    private void setupPaymentFaq() {
        layoutAdditionalInfo.setVisibility(View.VISIBLE);
        layoutRelatedLinks.setVisibility(View.VISIBLE);

        tvAdditionalInfo.setText("Tất cả thanh toán đều được bảo mật với công nghệ mã hóa SSL 256-bit. Chúng tôi không lưu trữ thông tin thẻ tín dụng của bạn.");

        chipRelated1.setText("Các phương thức thanh toán");
        chipRelated2.setText("Lịch sử giao dịch");
        chipRelated1.setOnClickListener(v -> showRelatedFaq("faq_payment_methods"));
        chipRelated2.setOnClickListener(v -> showRelatedFaq("faq_transaction_history"));
    }

    /**
     * Thiết lập nội dung cho FAQ giao hàng
     */
    private void setupDeliveryFaq() {
        layoutAdditionalInfo.setVisibility(View.VISIBLE);
        layoutRelatedLinks.setVisibility(View.VISIBLE);

        tvAdditionalInfo.setText("Thời gian giao hàng có thể thay đổi tùy thuộc vào địa điểm và điều kiện thời tiết. Chúng tôi sẽ thông báo cụ thể qua SMS và email.");

        chipRelated1.setText("Khu vực phục vụ");
        chipRelated2.setText("Phí giao hàng");
        chipRelated1.setOnClickListener(v -> showRelatedFaq("faq_service_area"));
        chipRelated2.setOnClickListener(v -> showRelatedFaq("faq_delivery_fee"));
    }

    /**
     * Thiết lập nội dung cho FAQ hoàn tiền
     */
    private void setupRefundFaq() {
        layoutAdditionalInfo.setVisibility(View.VISIBLE);
        layoutRelatedLinks.setVisibility(View.VISIBLE);

        tvAdditionalInfo.setText("Quy trình hoàn tiền sẽ được xử lý trong vòng 3-5 ngày làm việc sau khi xác nhận yêu cầu. Số tiền sẽ được hoàn về phương thức thanh toán ban đầu.");

        chipRelated1.setText("Chính sách đổi trả");
        chipRelated2.setText("Liên hệ hỗ trợ");
        chipRelated1.setOnClickListener(v -> showRelatedFaq("faq_return_policy"));
        chipRelated2.setOnClickListener(v -> contactSupport());
    }

    /**
     * Thiết lập click listeners
     */
    private void setupClickListeners() {
        btnClose.setOnClickListener(this);
        btnCloseDialog.setOnClickListener(this);
        btnContactSupport.setOnClickListener(this);
    }

    /**
     * Thiết lập animations
     */
    private void setupAnimations() {
        if (getView() != null) {
            getView().setAlpha(0f);
            getView().animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setStartDelay(100)
                    .start();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnClose || id == R.id.btnCloseDialog) {
            dismissWithAnimation();
        } else if (id == R.id.btnContactSupport) {
            contactSupport();
        }
    }

    /**
     * Hiển thị FAQ liên quan
     */
    private void showRelatedFaq(String faqId) {
        // Close current dialog
        dismiss();

        // Show related FAQ
        if (getActivity() instanceof SupportActivity) {
            ((SupportActivity) getActivity()).showFaqDetail(faqId);
        }
    }

    /**
     * Liên hệ hỗ trợ
     */
    private void contactSupport() {
        dismiss();

        if (getActivity() instanceof SupportActivity) {
            ((SupportActivity) getActivity()).handleCallSupport();
        }
    }

    /**
     * Đóng dialog với animation
     */
    private void dismissWithAnimation() {
        if (getView() != null) {
            getView().animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(this::dismiss)
                    .start();
        } else {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // Track analytics
        trackFaqClosed();
    }

    /**
     * Track analytics cho FAQ
     */
    private void trackFaqClosed() {
        // Implement analytics tracking
        // FirebaseAnalytics.getInstance(requireContext()).logEvent("faq_viewed", bundle);
    }
}
