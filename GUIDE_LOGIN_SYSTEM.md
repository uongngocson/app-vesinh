# 🔐 Hướng Dẫn Hệ Thống Đăng Nhập - Laundry App

## 📌 Tổng Quan

Đã tạo hoàn chỉnh hệ thống đăng nhập cho ứng dụng Laundry với:
- ✅ Giao diện đẹp, hiện đại
- ✅ Validation đầy đủ
- ✅ Session management
- ✅ Chức năng logout
- ✅ Code sạch, dễ mở rộng

## 📁 Các File Đã Tạo/Sửa

### Các File Mới Tạo:

```
app/src/main/java/com/project/laundryappui/
├── auth/
│   ├── LoginActivity.java           ✅ Activity đăng nhập
│   └── README_AUTH.md              ✅ Hướng dẫn chi tiết
├── utils/
│   ├── SessionManager.java         ✅ Quản lý session
│   └── InputValidator.java         ✅ Validate input

app/src/main/res/
├── layout/
│   └── activity_login.xml          ✅ Layout đăng nhập
├── drawable/
│   ├── bg_login_gradient.xml       ✅ Background gradient
│   ├── bg_button_primary.xml       ✅ Button style
│   ├── bg_edit_text_login.xml      ✅ Input field style
│   ├── bg_edit_text_focused.xml    ✅ Input focused style
│   ├── bg_edit_text_selector.xml   ✅ Input selector
│   ├── bg_card_white.xml           ✅ Card background
│   ├── ic_email.xml                ✅ Email icon
│   ├── ic_password.xml             ✅ Password icon
│   └── ic_logout.xml               ✅ Logout icon
```

### Các File Đã Sửa:

```
✅ MainActivity.java            - Thêm chức năng logout
✅ AndroidManifest.xml         - Đặt LoginActivity là launcher
✅ strings.xml                 - Thêm các text cho login
✅ menu_navigation_drawer.xml  - Thêm menu logout
```

## 🚀 Hướng Dẫn Build & Run

### Bước 1: Sync Project

1. Mở project trong Android Studio
2. Click **File** → **Sync Project with Gradle Files**
3. Đợi sync hoàn tất

### Bước 2: Build Project

```bash
# Trong terminal của Android Studio
./gradlew clean build
```

Hoặc click **Build** → **Rebuild Project**

### Bước 3: Run App

1. Kết nối thiết bị Android hoặc start emulator
2. Click nút **Run** (▶️) hoặc nhấn `Shift + F10`
3. Ứng dụng sẽ mở màn hình đăng nhập

## 🎯 Sử Dụng

### Đăng Nhập

**Thông tin demo account:**
```
Email: demo@laundry.com
Password: 123456
```

**Các bước:**
1. Mở app → Hiển thị màn hình đăng nhập
2. Nhập email: `demo@laundry.com`
3. Nhập password: `123456`
4. Click nút "Đăng Nhập"
5. ✅ Tự động chuyển đến MainActivity

### Đăng Xuất

1. Trong MainActivity, mở Navigation Drawer (click icon menu ☰)
2. Chọn "Đăng Xuất"
3. Xác nhận trong dialog
4. ✅ Quay về màn hình đăng nhập

### Auto-Login

- Sau khi đăng nhập thành công lần đầu, lần sau mở app sẽ **tự động đăng nhập**
- Để đăng nhập lại, cần đăng xuất trước

## 🎨 Giao Diện

### Màn Hình Đăng Nhập
- 🌈 Gradient background xanh lá đẹp mắt
- 🏢 Logo và title app ở trên
- 📝 Card trắng bo tròn cho form
- ✍️ Input fields với icon và animation
- 🔘 Button đăng nhập với ripple effect
- 🔗 Link "Quên mật khẩu?" và "Đăng ký"
- ⚡ Loading indicator khi đăng nhập

### Validation
- ✅ Email: Kiểm tra format hợp lệ
- ✅ Password: Tối thiểu 6 ký tự
- ✅ Hiển thị lỗi realtime khi user nhập sai

## 🔧 Mở Rộng

### 1. Tích Hợp API Backend

**File cần sửa:** `LoginActivity.java` → Method `performLogin()`

Thay code demo bằng API call thực tế:

```java
private void performLogin(String email, String password) {
    // TODO: Thay bằng API service của bạn
    ApiService.getInstance()
        .login(new LoginRequest(email, password))
        .enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse data = response.body();
                    onLoginSuccess(
                        data.getEmail(), 
                        data.getName(),
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
                    "Lỗi kết nối: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
}
```

**Setup Retrofit (nếu dùng):**

```gradle
// build.gradle (app level)
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}
```

### 2. Thêm Màn Hình Đăng Ký

Tạo `SignUpActivity.java` tương tự `LoginActivity.java`

**Update trong LoginActivity.java:**

```java
private void handleSignUp() {
    Intent intent = new Intent(this, SignUpActivity.class);
    startActivity(intent);
}
```

### 3. Thêm Forgot Password

Tạo `ForgotPasswordActivity.java`

**Update trong LoginActivity.java:**

```java
private void handleForgotPassword() {
    Intent intent = new Intent(this, ForgotPasswordActivity.class);
    startActivity(intent);
}
```

### 4. Thêm Remember Me

**Thêm CheckBox vào `activity_login.xml`:**

```xml
<CheckBox
    android:id="@+id/cbRememberMe"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Ghi nhớ đăng nhập"
    android:layout_marginTop="8dp" />
```

**Update SessionManager.java:**

```java
private static final String KEY_REMEMBER_ME = "rememberMe";

public void setRememberMe(boolean remember) {
    editor.putBoolean(KEY_REMEMBER_ME, remember);
    editor.apply();
}

public boolean shouldRememberMe() {
    return preferences.getBoolean(KEY_REMEMBER_ME, false);
}
```

### 5. Thêm Social Login (Google/Facebook)

**Google Sign-In:**

```gradle
implementation 'com.google.android.gms:play-services-auth:20.7.0'
```

Xem chi tiết tại: https://developers.google.com/identity/sign-in/android/start

**Facebook Login:**

```gradle
implementation 'com.facebook.android:facebook-login:16.2.0'
```

Xem chi tiết tại: https://developers.facebook.com/docs/facebook-login/android

### 6. Thêm Biometric Authentication (Vân tay)

```gradle
implementation 'androidx.biometric:biometric:1.2.0-alpha05'
```

```java
BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
    new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            navigateToMain();
        }
    });

BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
    .setTitle("Đăng nhập bằng vân tay")
    .setNegativeButtonText("Hủy")
    .build();

biometricPrompt.authenticate(promptInfo);
```

## 🔒 Bảo Mật (Production)

### Cần Implement:

1. **HTTPS:** Tất cả API calls phải dùng HTTPS
2. **Token Authentication:** Sử dụng JWT hoặc OAuth
3. **Encrypted Storage:** Mã hóa SharedPreferences

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

4. **Password Hashing:** Không lưu password plain text
5. **Rate Limiting:** Giới hạn số lần đăng nhập sai
6. **2FA:** Two-factor authentication

## ⚠️ Lưu Ý

### Trước Khi Build:

1. ✅ Sync Gradle
2. ✅ Clean project nếu cần
3. ✅ Đảm bảo có kết nối Internet (để tải dependencies)

### Nếu Gặp Lỗi:

**Lỗi "Cannot resolve symbol":**
- Sync project: **File** → **Sync Project with Gradle Files**
- Invalidate caches: **File** → **Invalidate Caches / Restart**

**Lỗi "R cannot be resolved":**
- Clean project: **Build** → **Clean Project**
- Rebuild: **Build** → **Rebuild Project**

**Lỗi layout:**
- Kiểm tra tất cả resources đã tạo đúng trong thư mục `res/`
- Xem log chi tiết trong **Build** tab

## 📊 Cấu Trúc Code

### LoginActivity.java
```
- validateInput()       → Kiểm tra email/password
- performLogin()        → Xử lý đăng nhập (demo)
- onLoginSuccess()      → Callback thành công
- onLoginFailed()       → Callback thất bại
- showLoading()         → Hiển thị loading
- navigateToMain()      → Chuyển đến MainActivity
```

### SessionManager.java
```
- createLoginSession()  → Lưu thông tin đăng nhập
- isLoggedIn()          → Kiểm tra trạng thái
- getUserEmail()        → Lấy email user
- getUserName()         → Lấy tên user
- logout()              → Xóa session
```

### InputValidator.java
```
- isValidEmail()        → Validate email
- isValidPassword()     → Validate password (≥6 ký tự)
- isStrongPassword()    → Validate password mạnh (≥8, có số, chữ hoa)
- isValidPhone()        → Validate số điện thoại VN
- isEmpty()             → Kiểm tra string rỗng
```

## ✅ Checklist

### Đã Hoàn Thành:
- [x] LoginActivity với giao diện đẹp
- [x] Input validation
- [x] SessionManager
- [x] Auto-login
- [x] Logout trong MainActivity
- [x] Loading state
- [x] Error handling
- [x] Code có comment đầy đủ

### Cần Làm Thêm (Tùy Chọn):
- [ ] Tích hợp API backend thật
- [ ] Màn hình đăng ký
- [ ] Màn hình quên mật khẩu
- [ ] Remember me
- [ ] Social login
- [ ] Biometric authentication
- [ ] Encrypted storage

## 🎓 Demo Flow

```
1. Mở app
   ↓
2. LoginActivity (Launcher)
   ↓
3. Nhập: demo@laundry.com / 123456
   ↓
4. Click "Đăng Nhập"
   ↓
5. Loading... (1.5s)
   ↓
6. SessionManager lưu thông tin
   ↓
7. MainActivity (Home Screen)
   ↓
8. Click menu ☰ → Đăng Xuất
   ↓
9. Xác nhận → Logout
   ↓
10. SessionManager xóa session
   ↓
11. Quay về LoginActivity
```

## 📞 Hỗ Trợ

Nếu cần hỗ trợ thêm:
- Đọc file `auth/README_AUTH.md` để biết chi tiết
- Xem code comment trong từng file
- Check Android Studio log nếu có lỗi

---

**Version:** 1.0.0  
**Created:** Dec 2024  
**Status:** ✅ Ready for Development

## 🎉 Kết Luận

Hệ thống đăng nhập đã sẵn sàng! 
- Code **sạch**, **chuẩn**, **dễ mở rộng**
- Giao diện **đẹp**, **hiện đại**
- Tất cả đã test và hoạt động tốt

Bạn chỉ cần:
1. Sync Gradle
2. Build project
3. Run và test với demo account
4. Tích hợp API backend khi có

**Happy Coding! 🚀**

