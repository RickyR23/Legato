package com.example.legatoapp.models.response;

import java.util.List;

public class SpotifyUserRecentlyPlayedTracksResponse {
    private Item item;  // Used for currently playing track
    private List<Item> items; // Used for recently played tracks
    private boolean is_playing; // Only exists in currently-playing response

    public boolean isPlaying() {
        return is_playing;
    }

    public Item getCurrentlyPlayingTrack() {
        return item; // For "/v1/me/player/currently-playing"
    }

    public Item getLastPlayedTrack() {
        return (items != null && !items.isEmpty()) ? items.get(0) : null;
    }

    public static class Item {
        private Track track;

        public Track getTrack() {
            return track;
        }
    }

    public static class Track {
        private String name;
        private List<Artist> artists;
        private Album album;

        public String getName() {
            return name;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public Album getAlbum() {
            return album;
        }

        public String getFirstArtist() {
            return (artists != null && !artists.isEmpty()) ? artists.get(0).getName() : "Unknown Artist";
        }
    }

    public static class Artist {
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class Album {
        private String name;
        private List<Image> images;

        public String getName() {
            return name;
        }

        public List<Image> getImages() {
            return images;
        }

        public String getAlbumImage() {
            return (images != null && !images.isEmpty()) ? images.get(0).getUrl() : null;
        }
    }

    public static class Image {
        private String url;

        public String getUrl() {
            return url;
        }
    }
}