package com.example.legatoapp.models;

import com.example.legatoapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MusicData {
    private static final List<Song> songList = new ArrayList<>();
    private static final List<String> artistList = new ArrayList<>();
    private static final Map<String, Integer> artistImages = new HashMap<>();

    static {
        // Initialize Song List
        songList.add(new Song("Count Me Out", "Kendrick Lamar", R.drawable.song10));
        songList.add(new Song("I Can't Help It", "Michael Jackson", R.drawable.song11));
        songList.add(new Song("Take it or Leave it", "The Strokes", R.drawable.song12));
        songList.add(new Song("505", "Arctic Monkeys", R.drawable.song13));
        songList.add(new Song("Sober", "Childish Gambino", R.drawable.song14));
        songList.add(new Song("Fancy", "Drake", R.drawable.song15));
        songList.add(new Song("Just", "Radiohead", R.drawable.song16));

        // Initialize Artist List & Artist Images (Matches `initializeArtistImages()`)
        artistList.add("The Marías");
        artistList.add("Frank Ocean");
        artistList.add("Daniel Caesar");
        artistList.add("Queen");
        artistList.add("Isaiah Rashad");
        artistList.add("Mac DeMarco");

        artistImages.put("The Marías", R.drawable.artist10);
        artistImages.put("Frank Ocean", R.drawable.artist11);
        artistImages.put("Daniel Caesar", R.drawable.artist12);
        artistImages.put("Queen", R.drawable.artist13);
        artistImages.put("Isaiah Rashad", R.drawable.artist14);
        artistImages.put("Mac DeMarco", R.drawable.artist15);
    }

    // Get all songs
    public static List<Song> getSongs() {
        return new ArrayList<>(songList);
    }

    // Get artist names from the separate artist list
    public static List<String> getArtistNames() {
        return new ArrayList<>(artistList);
    }

    // Get artist image for a specific artist
    public static int getArtistImage(String artist) {
        return artistImages.getOrDefault(artist, R.drawable.angelespfp);
    }

    // ✅ New Method: Get the artist images map
    public static Map<String, Integer> getArtistImageMap() {
        return new HashMap<>(artistImages); // Return a copy of the artistImages map
    }
}
