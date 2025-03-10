package com.example.legatoapp;

public class NotificationItem {
    private int profileImage;
    private String username;
    private String message;
    private String timestamp;
    private String notificationType; // New field

    public NotificationItem(int profileImage, String username, String message, String timestamp, String notificationType) {
        this.profileImage = profileImage;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.notificationType = notificationType;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNotificationType() {
        return notificationType;
    }
}
