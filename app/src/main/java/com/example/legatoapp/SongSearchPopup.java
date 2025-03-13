package com.example.legatoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SongSearchPopup {

    private Context context;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private SongAdapter songAdapter;
    private List<Song> songList;

    public SongSearchPopup(Context context) {
        this.context = context;
    }

    public void showPopup(View anchorView) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_song_search, null);

        // Initialize UI elements
        searchInput = popupView.findViewById(R.id.search_input);
        recyclerView = popupView.findViewById(R.id.recycler_view_songs);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        songList = getDummySongs(); // Dummy song data for testing
        songAdapter = new SongAdapter(songList);
        recyclerView.setAdapter(songAdapter);

        // Create and show the popup window
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(anchorView);


        // Add search functionality (basic filtering)
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Close popup when user clicks outside
        popupView.setOnTouchListener((v, event) -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            return true;
        });
    }

    private void filterSongs(String query) {
        List<Song> filteredList = new ArrayList<>();
        for (Song song : songList) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    song.getArtist().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(song);
            }
        }
        songAdapter.updateList(filteredList);
    }

    private List<Song> getDummySongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Blinding Lights", "The Weeknd", R.drawable.song_example));
        songs.add(new Song("Watermelon Sugar", "Harry Styles", R.drawable.song_example));
        songs.add(new Song("Save Your Tears", "The Weeknd", R.drawable.song_example));
        return songs;
    }
}
