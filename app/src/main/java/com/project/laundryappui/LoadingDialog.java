package com.project.laundryappui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * Dialog hiển thị loading với vòng tròn xoay khi đang tìm thợ đánh giày
 */
public class LoadingDialog extends Dialog {

    private ProgressBar progressBar;
    private TextView tvLoadingMessage;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        initDialog();
    }

    private void initDialog() {
        // Tạo dialog không có title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_loading, null);
        setContentView(view);

        // Thiết lập background trong suốt
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Khởi tạo views
        progressBar = view.findViewById(R.id.progress_bar);
        tvLoadingMessage = view.findViewById(R.id.tv_loading_message);

        // Thiết lập dialog properties
        setCancelable(false); // Không cho phép dismiss bằng back button
        setCanceledOnTouchOutside(false); // Không cho phép dismiss bằng touch outside
    }

    /**
     * Thiết lập message hiển thị
     */
    public void setLoadingMessage(String message) {
        if (tvLoadingMessage != null) {
            tvLoadingMessage.setText(message);
        }
    }

    /**
     * Thiết lập message từ string resource
     */
    public void setLoadingMessage(int stringResId) {
        setLoadingMessage(getContext().getString(stringResId));
    }
}
