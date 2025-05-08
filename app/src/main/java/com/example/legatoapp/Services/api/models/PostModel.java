package com.example.legatoapp.Services.api.models;

public class PostModel {


    private String user_ID;
    private String song_ID;
    private String caption;

    // Default constructor
    public PostModel() {
    }

    // Parameterized constructor
    public PostModel(String user_ID, String song_ID, String caption) {
        this.user_ID = user_ID;
        this.song_ID = song_ID;
        this.caption = caption;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }
    // Getters and Setters
    public String getSong_ID() {
        return song_ID;
    }

    public void setSong_ID(String song_ID) {
        this.song_ID = song_ID;
    }

    public String getDescription() {
        return caption;
    }

    public void setDescription(String description) {
        this.caption = description;
    }

    @Override
    public String toString() {
        return "Post{" +
                "user_ID='" + user_ID + '\'' +
                ", song_ID='" + song_ID + '\'' +
                ", description='" + caption + '\'' +
                '}';
    }
}
