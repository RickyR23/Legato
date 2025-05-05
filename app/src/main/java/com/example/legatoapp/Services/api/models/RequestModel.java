package com.example.legatoapp.Services.api.models;

public class RequestModel {
    private String email;
    private String username;
    private String spotify_ID;

    // Default constructor
    public RequestModel() {
    }

    // Parameterized constructor
    public RequestModel(String email, String username, String spotify_ID) {
        this.email = email;
        this.username = username;
        this.spotify_ID = spotify_ID;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSpotify_ID() {
        return spotify_ID;
    }

    public void setSpotify_ID(String spotify_ID) {
        this.spotify_ID = spotify_ID;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", spotify_ID='" + spotify_ID + '\'' +
                '}';
    }
}
