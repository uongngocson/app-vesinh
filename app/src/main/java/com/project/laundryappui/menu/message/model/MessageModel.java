package com.project.laundryappui.menu.message.model;

import java.util.Date;

/**
 * Model class cho tin nhắn
 * Support cả text và image messages
 */
public class MessageModel {

    public enum MessageType {
        TEXT,
        IMAGE
    }

    public enum MessageStatus {
        SENDING,
        SENT,
        DELIVERED,
        READ
    }

    private String id;
    private String content;
    private String imageUrl;
    private MessageType type;
    private boolean isSent; // true: tin nhắn gửi, false: tin nhắn nhận
    private String senderId;
    private String senderName;
    private Date timestamp;
    private MessageStatus status;

    // Constructor for text message
    public MessageModel(String id, String content, boolean isSent, String senderId, String senderName) {
        this.id = id;
        this.content = content;
        this.type = MessageType.TEXT;
        this.isSent = isSent;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = new Date();
        this.status = isSent ? MessageStatus.SENDING : MessageStatus.DELIVERED;
    }

    // Constructor for image message
    public MessageModel(String id, String content, String imageUrl, boolean isSent, String senderId, String senderName) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.type = MessageType.IMAGE;
        this.isSent = isSent;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = new Date();
        this.status = isSent ? MessageStatus.SENDING : MessageStatus.DELIVERED;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MessageType getType() {
        return type;
    }

    public boolean isSent() {
        return isSent;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    // Helper methods
    public boolean isTextMessage() {
        return type == MessageType.TEXT;
    }

    public boolean isImageMessage() {
        return type == MessageType.IMAGE;
    }

    public boolean isDelivered() {
        return status == MessageStatus.DELIVERED || status == MessageStatus.READ;
    }

    public boolean isRead() {
        return status == MessageStatus.READ;
    }
}
