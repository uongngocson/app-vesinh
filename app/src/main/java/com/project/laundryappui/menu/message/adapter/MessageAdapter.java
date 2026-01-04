package com.project.laundryappui.menu.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.laundryappui.R;
import com.project.laundryappui.menu.message.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter cho RecyclerView hiển thị danh sách tin nhắn
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<MessageModel> messages;
    private SimpleDateFormat timeFormat;

    public MessageAdapter(Context context, List<MessageModel> messages) {
        this.context = context;
        this.messages = messages;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messages.get(position);
        return message.isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_SENT) {
            View view = inflater.inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messages.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public void addMessage(MessageModel message) {
        if (messages != null) {
            messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }
    }

    public void updateMessageStatus(int position, MessageModel.MessageStatus status) {
        if (messages != null && position >= 0 && position < messages.size()) {
            messages.get(position).setStatus(status);
            notifyItemChanged(position);
        }
    }

    // ViewHolder for sent messages
    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageContent, tvTimestamp;
        ImageView ivMessageImage, ivMessageStatus;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageContent = itemView.findViewById(R.id.tv_message_content);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            ivMessageImage = itemView.findViewById(R.id.iv_message_image);
            ivMessageStatus = itemView.findViewById(R.id.iv_message_status);
        }

        void bind(MessageModel message) {
            tvTimestamp.setText(formatTime(message.getTimestamp()));

            if (message.isTextMessage()) {
                tvMessageContent.setVisibility(View.VISIBLE);
                tvMessageContent.setText(message.getContent());
                ivMessageImage.setVisibility(View.GONE);
            } else if (message.isImageMessage()) {
                tvMessageContent.setVisibility(View.GONE);
                ivMessageImage.setVisibility(View.VISIBLE);
                // For now, just show a placeholder. In production, use Glide or Picasso
                ivMessageImage.setImageResource(R.drawable.ic_more);
            }

            // Update message status icon
            updateStatusIcon(ivMessageStatus, message.getStatus());
        }
    }

    // ViewHolder for received messages
    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderName, tvMessageContent, tvTimestamp;
        ImageView ivMessageImage, ivAvatar;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            tvMessageContent = itemView.findViewById(R.id.tv_message_content);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            ivMessageImage = itemView.findViewById(R.id.iv_message_image);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }

        void bind(MessageModel message) {
            tvTimestamp.setText(formatTime(message.getTimestamp()));

            // Show sender name for group chats or different senders
            if (message.getSenderName() != null && !message.getSenderName().isEmpty()) {
                tvSenderName.setVisibility(View.VISIBLE);
                tvSenderName.setText(message.getSenderName());
            } else {
                tvSenderName.setVisibility(View.GONE);
            }

            if (message.isTextMessage()) {
                tvMessageContent.setVisibility(View.VISIBLE);
                tvMessageContent.setText(message.getContent());
                ivMessageImage.setVisibility(View.GONE);
            } else if (message.isImageMessage()) {
                tvMessageContent.setVisibility(View.GONE);
                ivMessageImage.setVisibility(View.VISIBLE);
                // For now, just show a placeholder. In production, use Glide or Picasso
                ivMessageImage.setImageResource(R.drawable.ic_more);
            }

            // Load avatar if available
            if (message.getSenderId() != null) {
                // You can implement avatar loading logic here
                // For now, using default avatar
                ivAvatar.setImageResource(R.drawable.ic_user);
            }
        }
    }

    private String formatTime(Date date) {
        return timeFormat.format(date);
    }

    private void updateStatusIcon(ImageView statusIcon, MessageModel.MessageStatus status) {
        int iconRes;
        switch (status) {
            case SENT:
            case DELIVERED:
            case READ:
                iconRes = R.drawable.ic_message_sent;
                break;
            default:
                // For sending status, hide the icon
                statusIcon.setVisibility(View.GONE);
                return;
        }
        statusIcon.setVisibility(View.VISIBLE);
        statusIcon.setImageResource(iconRes);
    }
}
