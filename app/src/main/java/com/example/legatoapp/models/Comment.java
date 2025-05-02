package com.example.legatoapp.models;

public class Comment {
    private String username;
    private String text;
    private int imageResId;
    private String timestamp;  // <-- Add this

    public Comment(String username, String text, int imageResId, String timestamp) {
        this.username = username;
        this.text = text;
        this.imageResId = imageResId;
        this.timestamp = timestamp; // <-- Set the field
    }

    public String getUsername() { return username; }
    public String getText() { return text; }
    public int getImageResId() { return imageResId; }
    public String getTimestamp() { return timestamp; } // <-- Add getter
}
