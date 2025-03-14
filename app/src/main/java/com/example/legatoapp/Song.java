package com.example.legatoapp;

public class Song {
    private String title;
    private String artist;
    private int albumArt; // Drawable resource ID for the album artwork

    public Song(String title, String artist, int albumArt) {
        this.title = title;
        this.artist = artist;
        this.albumArt = albumArt;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getAlbumArt() { return albumArt; }
}
