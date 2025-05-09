package com.example.legatoapp;
public class Post {
    private String username;
    private String caption;
    private String musicTrack;
    private int albumResId; // resource ID for album cover
    private String spotifyUrl;

    public Post(String username, String caption, String musicTrack, int albumResId, String spotifyUrl) {
        this.username = username;
        this.caption = caption;
        this.musicTrack = musicTrack;
        this.albumResId = albumResId;
        this.spotifyUrl = spotifyUrl;
    }

    public String getUsername() { return username; }
    public String getCaption() { return caption; }
    public String getMusicTrack() { return musicTrack; }
    public int getAlbumResId() { return albumResId; }
    public String getSpotifyUrl() { return spotifyUrl; }
}
