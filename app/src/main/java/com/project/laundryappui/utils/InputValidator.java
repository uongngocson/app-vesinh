package com.project.laundryappui.utils;

import android.util.Patterns;

/**
 * InputValidator - Validate các input từ người dùng
 * Dễ dàng mở rộng thêm các phương thức validate khác
 */
public class InputValidator {

    /**
     * Validate địa chỉ email
     * @param email Email cần validate
     * @return true nếu email hợp lệ, false nếu không
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    /**
     * Validate password
     * Password phải có ít nhất 6 ký tự
     * @param password Password cần validate
     * @return true nếu password hợp lệ, false nếu không
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }

    /**
     * Validate password mạnh (dùng cho đăng ký)
     * Password phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số
     * @param password Password cần validate
     * @return true nếu password mạnh, false nếu không
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isLowerCase(c)) hasLowerCase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpperCase && hasLowerCase && hasDigit;
    }

    /**
     * Validate số điện thoại (Việt Nam)
     * @param phone Số điện thoại cần validate
     * @return true nếu số điện thoại hợp lệ, false nếu không
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Regex cho số điện thoại Việt Nam
        String phonePattern = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$";
        return phone.trim().matches(phonePattern);
    }

    /**
     * Validate tên người dùng
     * @param name Tên cần validate
     * @return true nếu tên hợp lệ, false nếu không
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().length() >= 2;
    }

    /**
     * Kiểm tra xem string có rỗng không
     * @param text Text cần kiểm tra
     * @return true nếu rỗng hoặc null, false nếu có nội dung
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}
