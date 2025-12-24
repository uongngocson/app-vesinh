package com.project.laundryappui.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * User - Model thông tin người dùng
 * Map với User entity từ NestJS backend
 */
public class User {
    private String id;
    private String email;
    private String phone;
    private String fullName;
    private String gender;
    private String dateOfBirth;
    private String avatarUrl;
    private String role;
    private String status;
    private String emailVerifiedAt;
    private String phoneVerifiedAt;
    private String lastLoginAt;
    private String lastLoginIp;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    @SerializedName("deleted_at")
    private String deletedAt;
    
    // Membership info (nếu có)
    private Membership membership;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPhoneVerifiedAt() {
        return phoneVerifiedAt;
    }

    public void setPhoneVerifiedAt(String phoneVerifiedAt) {
        this.phoneVerifiedAt = phoneVerifiedAt;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(String lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    /**
     * Membership - Inner class cho thông tin membership
     */
    public static class Membership {
        private String id;
        private String userId;
        private String tierId;
        private String currentTier;
        private int totalPoints;
        private int lifetimePoints;
        private String accountBalance;
        private String totalSpent;
        private int totalOrders;
        private String memberSince;
        private String tierExpiresAt;
        
        @SerializedName("created_at")
        private String createdAt;
        
        @SerializedName("updated_at")
        private String updatedAt;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTierId() {
            return tierId;
        }

        public void setTierId(String tierId) {
            this.tierId = tierId;
        }

        public String getCurrentTier() {
            return currentTier;
        }

        public void setCurrentTier(String currentTier) {
            this.currentTier = currentTier;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(int totalPoints) {
            this.totalPoints = totalPoints;
        }

        public int getLifetimePoints() {
            return lifetimePoints;
        }

        public void setLifetimePoints(int lifetimePoints) {
            this.lifetimePoints = lifetimePoints;
        }

        public String getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(String accountBalance) {
            this.accountBalance = accountBalance;
        }

        public String getTotalSpent() {
            return totalSpent;
        }

        public void setTotalSpent(String totalSpent) {
            this.totalSpent = totalSpent;
        }

        public int getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(int totalOrders) {
            this.totalOrders = totalOrders;
        }

        public String getMemberSince() {
            return memberSince;
        }

        public void setMemberSince(String memberSince) {
            this.memberSince = memberSince;
        }

        public String getTierExpiresAt() {
            return tierExpiresAt;
        }

        public void setTierExpiresAt(String tierExpiresAt) {
            this.tierExpiresAt = tierExpiresAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}

