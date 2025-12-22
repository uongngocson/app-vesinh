# 🔐 Hệ Thống Xác Thực (Authentication System)

## 📋 Tổng Quan

Hệ thống đăng nhập hoàn chỉnh cho ứng dụng Laundry với giao diện đẹp, code sạch và dễ mở rộng.

## 🏗️ Cấu Trúc

```
auth/
├── LoginActivity.java          # Màn hình đăng nhập
└── README_AUTH.md             # File này

utils/
├── SessionManager.java        # Quản lý phiên đăng nhập
└── InputValidator.java        # Validate input

res/
├── layout/
│   └── activity_login.xml     # Layout màn hình đăng nhập
├── drawable/
│   ├── bg_login_gradient.xml
│   ├── bg_button_primary.xml
│   ├── bg_edit_text_*.xml
│   ├── ic_email.xml
│   └── ic_password.xml
└── values/
    └── strings.xml            # Các text cho login
```

## 🚀 Chức Năng Hiện Tại

### ✅ Đã Hoàn Thành

1. **Màn hình đăng nhập đẹp**
   - Gradient background
   - Material Design input fields
   - Animation và ripple effects
   - Responsive layout

2. **Validation đầy đủ**
   - Email format validation
   - Password length validation
   - Hiển thị lỗi real-time

3. **Session Management**
   - Lưu thông tin đăng nhập
   - Kiểm tra trạng thái đăng nhập
   - Auto-login nếu đã đăng nhập

4. **Demo Account**
   - Email: `demo@laundry.com`
   - Password: `123456`

## 📝 Hướng Dẫn Sử Dụng

### Đăng Nhập

1. Mở app → Hiển thị LoginActivity
2. Nhập email: `demo@laundry.com`
3. Nhập password: `123456`
4. Click "Đăng Nhập"
5. Tự động chuyển đến MainActivity

### Đăng Xuất (Thêm vào MainActivity)

```java
// Trong MainActivity.java
private void logout() {
    SessionManager sessionManager = new SessionManager(this);
    sessionManager.logout();
    
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
}
```

## 🔧 Mở Rộng

### 1. Tích Hợp API Thực Tế

**File:** `LoginActivity.java` → Method `performLogin()`

```java
private void performLogin(String email, String password) {
    // Thay demo code bằng API call
    ApiService.getInstance()
        .login(email, password)
        .enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse data = response.body();
                    onLoginSuccess(
                        data.getEmail(), 
                        data.getUserName(),
                        data.getUserId(),
                        data.getToken()
                    );
                } else {
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, 
                    getString(R.string.login_error), 
                    Toast.LENGTH_LONG).show();
            }
        });
}
```

### 2. Thêm Màn Hình Đăng Ký

**Tạo file:** `SignUpActivity.java`

```java
// Tương tự LoginActivity
// Validate: email, password, confirm password, name
// API: apiService.register(user)
```

**Update:** `LoginActivity.java` → Method `handleSignUp()`

```java
private void handleSignUp() {
    Intent intent = new Intent(this, SignUpActivity.class);
    startActivity(intent);
}
```

### 3. Thêm Forgot Password

**Tạo file:** `ForgotPasswordActivity.java`

```java
// Input: Email
// API: apiService.sendResetPasswordEmail(email)
// Hiển thị thông báo đã gửi email
```

### 4. Thêm Remember Me

**Update:** `activity_login.xml` - Thêm CheckBox

```xml
<CheckBox
    android:id="@+id/cbRememberMe"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Ghi nhớ đăng nhập" />
```

**Update:** `SessionManager.java`

```java
private static final String KEY_REMEMBER_ME = "rememberMe";

public void setRememberMe(boolean remember) {
    editor.putBoolean(KEY_REMEMBER_ME, remember);
    editor.apply();
}
```

### 5. Thêm Biometric Login (Vân Tay / FaceID)

**Dependencies:** `build.gradle`

```gradle
implementation 'androidx.biometric:biometric:1.2.0-alpha05'
```

**Code:**

```java
BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
    new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            // Auto login
            navigateToMain();
        }
    });

BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
    .setTitle("Đăng nhập bằng vân tay")
    .setNegativeButtonText("Hủy")
    .build();

biometricPrompt.authenticate(promptInfo);
```

### 6. Thêm Social Login (Google, Facebook)

**Google Sign-In:**

```gradle
implementation 'com.google.android.gms:play-services-auth:20.7.0'
```

**Facebook Login:**

```gradle
implementation 'com.facebook.android:facebook-login:16.2.0'
```

## 🔒 Bảo Mật

### Hiện Tại
- Input validation
- Session storage trong SharedPreferences

### Nên Thêm (Production)
- Encrypt password trước khi gửi API
- Sử dụng HTTPS
- Token authentication (JWT)
- Refresh token mechanism
- Store token an toàn (EncryptedSharedPreferences)

```gradle
implementation 'androidx.security:security-crypto:1.1.0-alpha06'
```

```java
MasterKey masterKey = new MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build();

SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
);
```

## 📦 Dependencies Cần Thiết

Đã có sẵn trong project:
- Material Components
- AppCompat
- ConstraintLayout

## ✅ Checklist Kiểm Tra

- [x] Layout đẹp và responsive
- [x] Input validation đầy đủ
- [x] Session management
- [x] Auto-login nếu đã đăng nhập
- [x] Loading state khi đăng nhập
- [x] Error handling
- [x] Code sạch, có comment
- [ ] Tích hợp API thực tế (TODO)
- [ ] Thêm đăng ký (TODO)
- [ ] Thêm forgot password (TODO)
- [ ] Thêm logout trong MainActivity (TODO)

## 📞 Support

Nếu cần hỗ trợ hoặc có thắc mắc, vui lòng tạo issue hoặc liên hệ dev team.

---

**Created:** Dec 2024  
**Last Updated:** Dec 2024  
**Version:** 1.0.0

