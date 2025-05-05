package com.example.legatoapp.Services.api.models;

public class PostModel {
    private String song_ID;
    private String description;

    // Default constructor
    public PostModel() {
    }

    // Parameterized constructor
    public PostModel(String song_ID, String description) {
        this.song_ID = song_ID;
        this.description = description;
    }

    // Getters and Setters
    public String getSong_ID() {
        return song_ID;
    }

    public void setSong_ID(String song_ID) {
        this.song_ID = song_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Post{" +
                "song_ID='" + song_ID + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
