package com.project.laundryappui.menu.message;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;
import com.project.laundryappui.menu.message.adapter.MessageAdapter;
import com.project.laundryappui.menu.message.model.MessageModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment chuyên nghiệp cho tính năng nhắn tin
 * Support real-time messaging, image sharing, và dễ mở rộng
 */
public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private static final int REQUEST_IMAGE_PICK = 103;

    // UI Components
    private RecyclerView recyclerViewMessages;
    private EditText etMessage;
    private ImageButton btnSend, btnAttach, btnCamera, btnEmoji, btnBack;
    private TextView tvTypingIndicator;
    private RelativeLayout emptyStateContainer, loadingContainer;

    // Data
    private MessageAdapter messageAdapter;
    private List<MessageModel> messages;
    private LinearLayoutManager layoutManager;

    // State
    private String currentPhotoPath;
    private boolean isTyping = false;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        setupTextWatcher();
        loadMessages();

        // Check if there are messages to show empty state
        updateEmptyState();
    }

    private void initializeViews(View view) {
        // RecyclerView
        recyclerViewMessages = view.findViewById(R.id.recyclerview_messages);

        // Input components
        etMessage = view.findViewById(R.id.et_message);
        btnSend = view.findViewById(R.id.btn_send);
        btnAttach = view.findViewById(R.id.btn_attach);
        btnCamera = view.findViewById(R.id.btn_camera);
        btnEmoji = view.findViewById(R.id.btn_emoji);
        btnBack = view.findViewById(R.id.btn_back);

        // Other UI
        tvTypingIndicator = view.findViewById(R.id.tv_typing_indicator);
        emptyStateContainer = view.findViewById(R.id.empty_state_container);
        loadingContainer = view.findViewById(R.id.loading_container);
    }

    private void setupRecyclerView() {
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(requireContext(), messages);

        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true); // Show latest messages at bottom

        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setHasFixedSize(true);

        // Auto scroll to bottom when new message is added
        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = messageAdapter.getItemCount();
                if (positionStart == messageCount - itemCount) {
                    recyclerViewMessages.scrollToPosition(messageCount - 1);
                }
            }
        });
    }

    private void setupClickListeners() {
        // Send button
        btnSend.setOnClickListener(v -> sendMessage());

        // Attach button (gallery)
        btnAttach.setOnClickListener(v -> openGallery());

        // Camera button
        btnCamera.setOnClickListener(v -> openCamera());

        // Emoji button (placeholder for future implementation)
        btnEmoji.setOnClickListener(v -> showEmojiPicker());

        // Back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setupTextWatcher() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSendButtonState();
                handleTypingIndicator(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle Enter key to send message
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void updateSendButtonState() {
        String message = etMessage.getText().toString().trim();
        btnSend.setEnabled(!message.isEmpty());
        btnSend.setAlpha(message.isEmpty() ? 0.5f : 1.0f);
    }

    private void handleTypingIndicator(boolean isTyping) {
        this.isTyping = isTyping;
        // In real implementation, you would emit typing events to server
        // For now, just update UI locally
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        // Create new message
        String messageId = generateMessageId();
        MessageModel message = new MessageModel(
                messageId,
                messageText,
                true, // isSent
                "user_id", // current user id
                "You"
        );

        // Add to list and update UI
        messages.add(message);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        etMessage.setText("");
        updateEmptyState();

        // Simulate message sending (in real app, this would be API call)
        simulateMessageSending(message);
    }

    private void openGallery() {
        if (checkGalleryPermission()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        } else {
            requestGalleryPermission();
        }
    }

    private void openCamera() {
        if (checkCameraPermission()) {
            dispatchTakePictureIntent();
        } else {
            requestCameraPermission();
        }
    }

    private void showEmojiPicker() {
        // Placeholder for emoji picker implementation
        Toast.makeText(requireContext(), "Emoji picker sẽ được thêm sau", Toast.LENGTH_SHORT).show();
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(requireContext(), "Lỗi tạo file ảnh", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.project.laundryappui.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir("Pictures");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requireActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (currentPhotoPath != null) {
                        sendImageMessage(currentPhotoPath);
                    }
                    break;

                case REQUEST_IMAGE_PICK:
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        sendImageMessage(imageUri.toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(requireContext(), "Cần quyền truy cập camera", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_GALLERY_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(requireContext(), "Cần quyền truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void sendImageMessage(String imagePath) {
        String messageId = generateMessageId();
        MessageModel message = new MessageModel(
                messageId,
                "", // content (empty for image messages)
                imagePath, // imageUrl
                true, // isSent
                "user_id",
                "You"
        );
        messages.add(message);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        updateEmptyState();

        // Simulate image upload
        simulateImageUploading(message);
    }

    private void simulateMessageSending(MessageModel message) {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            // Update message status to SENT
            int position = messages.indexOf(message);
            if (position != -1) {
                message.setStatus(MessageModel.MessageStatus.SENT);
                messageAdapter.updateMessageStatus(position, MessageModel.MessageStatus.SENT);

                // Simulate delivery
                new android.os.Handler().postDelayed(() -> {
                    message.setStatus(MessageModel.MessageStatus.DELIVERED);
                    messageAdapter.updateMessageStatus(position, MessageModel.MessageStatus.DELIVERED);
                }, 1000);
            }
        }, 500);
    }

    private void simulateImageUploading(MessageModel message) {
        // Show uploading status
        Toast.makeText(requireContext(), "Đang tải ảnh lên...", Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(() -> {
            int position = messages.indexOf(message);
            if (position != -1) {
                message.setStatus(MessageModel.MessageStatus.SENT);
                messageAdapter.updateMessageStatus(position, MessageModel.MessageStatus.SENT);
                Toast.makeText(requireContext(), "Ảnh đã gửi thành công", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    private void loadMessages() {
        setLoading(true);

        // Simulate loading messages from server
        new android.os.Handler().postDelayed(() -> {
            // Add some sample messages
            addSampleMessages();
            setLoading(false);
            updateEmptyState();
        }, 1000);
    }

    private void addSampleMessages() {
        // Sample received message
        MessageModel receivedMessage = new MessageModel(
                "msg_1",
                "Xin chào! Chúng tôi có thể giúp gì cho bạn hôm nay?",
                false,
                "support_id",
                "Support Team"
        );
        receivedMessage.setStatus(MessageModel.MessageStatus.DELIVERED);
        messages.add(receivedMessage);

        // Sample sent message
        MessageModel sentMessage = new MessageModel(
                "msg_2",
                "Tôi cần hỏi về dịch vụ giặt giày",
                true,
                "user_id",
                "You"
        );
        sentMessage.setStatus(MessageModel.MessageStatus.DELIVERED);
        messages.add(sentMessage);

        // Sample received message with image
        MessageModel receivedImageMessage = new MessageModel(
                "msg_3",
                "", // content (empty for image messages)
                "drawable://" + R.drawable.bg_post1, // Sample image
                false,
                "support_id",
                "Support Team"
        );
        receivedImageMessage.setStatus(MessageModel.MessageStatus.DELIVERED);
        messages.add(receivedImageMessage);

        messageAdapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        boolean isEmpty = messages == null || messages.isEmpty();
        emptyStateContainer.setVisibility(isEmpty && !isLoading ? View.VISIBLE : View.GONE);
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        loadingContainer.setVisibility(loading ? View.VISIBLE : View.GONE);
        emptyStateContainer.setVisibility(loading ? View.GONE : (messages.isEmpty() ? View.VISIBLE : View.GONE));
    }

    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cleanup resources if needed
        if (messageAdapter != null) {
            messageAdapter = null;
        }
    }
}