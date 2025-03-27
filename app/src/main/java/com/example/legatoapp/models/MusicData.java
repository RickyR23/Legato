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
        songList.add(new Song("Blinding Lights", "The Weeknd", R.drawable.snoopypfp));
        songList.add(new Song("Watermelon Sugar", "Harry Styles", R.drawable.snoopypfp));
        songList.add(new Song("Save Your Tears", "The Weeknd", R.drawable.snoopypfp));
        songList.add(new Song("Shape of You", "Ed Sheeran", R.drawable.snoopypfp));
        songList.add(new Song("Levitating", "Dua Lipa", R.drawable.song_example));
        songList.add(new Song("Circles", "Post Malone", R.drawable.song_example));
        songList.add(new Song("Good 4 U", "Olivia Rodrigo", R.drawable.song_example));

        // Initialize Artist List & Artist Images (Matches `initializeArtistImages()`)
        artistList.add("The Weeknd");
        artistList.add("Tyler");
        artistList.add("Angeles");
        artistList.add("Taylor");
        artistList.add("Adele");
        artistList.add("Bruno Mars");

        artistImages.put("The Weeknd", R.drawable.rickypfp);
        artistImages.put("Tyler", R.drawable.artist1);
        artistImages.put("Angeles", R.drawable.artist2);
        artistImages.put("Taylor", R.drawable.artist3);
        artistImages.put("Adele", R.drawable.artist1);
        artistImages.put("Bruno Mars", R.drawable.rickypfp);
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
