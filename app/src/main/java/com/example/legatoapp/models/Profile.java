package com.example.legatoapp.models;
public class Profile {
    private String displayName;
    private String username;
    private int profileImageResId;

    public Profile(String displayName, String username, int profileImageResId) {
        this.displayName = displayName;
        this.username = username;
        this.profileImageResId = profileImageResId;
    }

    // Getters
    public String getDisplayName() { return displayName; }
    public String getUsername() { return username; }
    public int getProfileImageResId() { return profileImageResId; }
}
