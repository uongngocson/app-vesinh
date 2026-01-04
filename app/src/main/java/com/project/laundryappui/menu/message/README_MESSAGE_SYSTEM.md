# Hệ Thống Nhắn Tin - Laundry App UI

## 📱 Tổng quan

Hệ thống nhắn tin chuyên nghiệp được tích hợp vào Laundry App, cung cấp giao tiếp real-time giữa khách hàng và đội ngũ hỗ trợ. Hệ thống được thiết kế với architecture mở rộng, UI/UX hiện đại và khả năng scale tốt.

## 🏗️ Kiến trúc

### Cấu trúc Files

```
menu/message/
├── MessageFragment.java                 # Main fragment xử lý UI và logic
├── adapter/
│   └── MessageAdapter.java             # RecyclerView adapter cho danh sách tin nhắn
├── model/
│   └── MessageModel.java               # Data model cho tin nhắn
└── README_MESSAGE_SYSTEM.md           # Tài liệu này
```

### Layout Files

```
res/layout/
├── fragment_message.xml               # Layout chính của message screen
├── item_message_sent.xml              # Layout item tin nhắn gửi
└── item_message_received.xml          # Layout item tin nhắn nhận
```

## 🎯 Tính năng chính

### ✅ Đã hoàn thành

#### 1. **Gửi tin nhắn text**
- Input validation
- Real-time feedback
- Auto-scroll đến tin nhắn mới
- Status tracking (Sending → Sent → Delivered)

#### 2. **Gửi hình ảnh**
- Camera capture
- Gallery picker
- Image compression (placeholder)
- Upload simulation

#### 3. **UI/UX chuyên nghiệp**
- Material Design compliance
- Responsive layout
- Keyboard handling
- Loading states
- Empty states
- Typing indicator

#### 4. **Permissions Management**
- Camera permission
- Storage permission
- Runtime permission handling
- Graceful error handling

### 🚧 Sẵn sàng mở rộng (Future Features)

#### 1. **Real-time Communication**
- WebSocket integration
- Firebase integration
- Push notifications

#### 2. **Advanced Messaging**
- Voice messages
- File attachments
- Message reactions
- Message replies
- Message search

#### 3. **Group Chat**
- Multiple participants
- Group management
- Admin features

#### 4. **Rich Media**
- Video messages
- Location sharing
- Contact sharing

## 📋 API Reference

### MessageModel

```java
public class MessageModel {
    // Enums
    enum MessageType { TEXT, IMAGE }
    enum MessageStatus { SENDING, SENT, DELIVERED, READ }

    // Constructors
    MessageModel(String id, String content, boolean isSent, String senderId, String senderName)
    MessageModel(String id, String content, String imageUrl, boolean isSent, String senderId, String senderName)

    // Properties
    String id, content, imageUrl, senderId, senderName
    MessageType type
    MessageStatus status
    Date timestamp
    boolean isSent
}
```

### MessageAdapter

```java
public class MessageAdapter extends RecyclerView.Adapter {
    // Constructor
    MessageAdapter(Context context, List<MessageModel> messages)

    // Methods
    void addMessage(MessageModel message)
    void updateMessageStatus(int position, MessageStatus status)
}
```

### MessageFragment

```java
public class MessageFragment extends Fragment {
    // Key methods
    void sendMessage()                    // Gửi tin nhắn text
    void openGallery()                    // Mở gallery picker
    void openCamera()                     // Mở camera
    void simulateMessageSending()         // Simulate gửi tin nhắn
    void simulateImageUploading()         // Simulate upload ảnh
}
```

## 🔧 Cấu hình

### AndroidManifest.xml

```xml
<!-- Permissions -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<!-- File Provider -->
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="com.project.laundryappui.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

### Dependencies (gradle)

```gradle
dependencies {
    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.2.1'

    // Image loading (recommended for production)
    implementation 'com.github.bumptech.glide:glide:4.13.2'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.13.2'

    // Alternative: Picasso
    implementation 'com.squareup.picasso:picasso:2.8'
}
```

## 🎨 UI Components

### Main Layout Structure

```
┌─────────────────────────────────────┐
│           Toolbar                    │
│  ┌─────────────────────────────────┐ │
│  │         Message List            │ │
│  │         (RecyclerView)          │ │
│  └─────────────────────────────────┘ │
│  ┌─────────────────────────────────┐ │
│  │   Input Container               │ │
│  │ ┌─┬─┬─────────────────┬─┐      │ │
│  │ │📎│📷│  Type message  │😊│      │ │
│  │ └─┴─┴─────────────────┴─┘      │ │
│  └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

### Message Item Types

#### Sent Message
- Background: Primary color với rounded corners
- Text color: White
- Status icon: Check mark
- Alignment: Right

#### Received Message
- Background: Light gray với rounded corners
- Text color: Dark gray
- Avatar: Circle image
- Sender name: Optional for group chat
- Alignment: Left

## 🚀 Cách sử dụng

### 1. Basic Message Sending

```java
// Tạo tin nhắn
MessageModel message = new MessageModel(
    "msg_123",
    "Hello, tôi cần hỗ trợ!",
    true, // isSent
    "user_456",
    "Nguyễn Văn A"
);

// Thêm vào adapter
messageAdapter.addMessage(message);
```

### 2. Image Message Sending

```java
MessageModel imageMessage = new MessageModel(
    "msg_124",
    "", // content empty for images
    "path/to/image.jpg",
    true,
    "user_456",
    "Nguyễn Văn A"
);

messageAdapter.addMessage(imageMessage);
```

### 3. Status Updates

```java
// Update status to SENT
messageAdapter.updateMessageStatus(position, MessageStatus.SENT);

// Update status to DELIVERED
messageAdapter.updateMessageStatus(position, MessageStatus.DELIVERED);
```

## 🔄 Integration với Backend

### WebSocket Integration

```java
// Example WebSocket setup
private void setupWebSocket() {
    // Initialize WebSocket client
    // Connect to server
    // Listen for incoming messages
    // Handle connection states
}

private void sendMessageToServer(MessageModel message) {
    // Convert to JSON
    // Send via WebSocket
    // Handle response
}
```

### REST API Integration

```java
private void sendMessageViaAPI(MessageModel message) {
    // Create API call
    // Upload image if needed
    // Handle response
    // Update local status
}
```

## 📊 Performance Considerations

### Memory Management
- Image compression before sending
- RecyclerView với ViewHolder pattern
- Bitmap recycling
- Message pagination

### Network Optimization
- Image compression
- Chunked uploads for large files
- Retry mechanisms
- Offline queue

### UI Performance
- RecyclerView optimizations
- DiffUtil for updates
- Background processing
- Smooth scrolling

## 🧪 Testing

### Unit Tests

```java
@Test
public void testMessageModelCreation() {
    MessageModel message = new MessageModel("id", "content", true, "sender", "name");
    assertEquals("id", message.getId());
    assertEquals("content", message.getContent());
    assertTrue(message.isSent());
}

@Test
public void testMessageAdapterOperations() {
    MessageAdapter adapter = new MessageAdapter(context, messages);
    adapter.addMessage(message);
    assertEquals(1, adapter.getItemCount());
}
```

### Integration Tests

```java
@Test
public void testMessageSendingFlow() {
    // Test full sending flow
    // Mock API responses
    // Verify UI updates
    // Check message status changes
}
```

## 🔒 Security Considerations

### Data Protection
- Message encryption at rest
- Secure file storage
- Permission validation
- Input sanitization

### Privacy
- Media access controls
- Message retention policies
- User data handling
- GDPR compliance

## 📈 Monitoring & Analytics

### Message Metrics
- Message send success rate
- Average response time
- User engagement
- Error rates

### Performance Metrics
- App startup time
- Memory usage
- Network usage
- Battery consumption

## 🚨 Troubleshooting

### Common Issues

#### 1. Images not loading
- Check Glide dependency
- Verify image paths
- Check storage permissions

#### 2. Messages not sending
- Check network connection
- Verify API endpoints
- Check authentication

#### 3. UI not updating
- Check RecyclerView adapter
- Verify data binding
- Check background thread usage

### Debug Mode

```java
// Enable debug logging
private static final boolean DEBUG = BuildConfig.DEBUG;

if (DEBUG) {
    Log.d(TAG, "Message sent: " + message.getContent());
}
```

## 🔮 Future Roadmap

### Phase 1 (Next Sprint)
- [ ] Real-time WebSocket integration
- [ ] Push notifications
- [ ] Message reactions
- [ ] Message search

### Phase 2 (Next Month)
- [ ] Voice messages
- [ ] File attachments
- [ ] Group chat
- [ ] Message encryption

### Phase 3 (Next Quarter)
- [ ] Video calls
- [ ] Screen sharing
- [ ] Advanced moderation
- [ ] Analytics dashboard

## 📞 Support

### Contact Information
- **Technical Lead**: [Your Name]
- **Email**: support@laundryapp.com
- **Documentation**: [Link to docs]

### Issue Reporting
- Use GitHub Issues for bugs
- Use Pull Requests for features
- Tag with `message-system` label

---

**Version**: 1.0.0
**Last Updated**: December 2024
**Authors**: Laundry App Development Team
