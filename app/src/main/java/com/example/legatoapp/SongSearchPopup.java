package com.example.legatoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;

public class SongSearchPopup {

    private Context context;
    private PopupWindow popupWindow;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private SongAdapter songAdapter;
    private List<Song> songList;
    private OnSongSelectedListener listener;

    public interface OnSongSelectedListener {
        void onSongSelected(Song song);
    }

    public SongSearchPopup(Context context, OnSongSelectedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void showPopup(View anchorView) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_song_search, null);

        searchInput = popupView.findViewById(R.id.search_input);
        recyclerView = popupView.findViewById(R.id.recycler_view_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        songList = getDummySongs();
        songAdapter = new SongAdapter(new ArrayList<>(songList), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                if (listener != null) {
                    listener.onSongSelected(song);
                }
                popupWindow.dismiss();
            }
        });

        recyclerView.setAdapter(songAdapter);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(anchorView);

        // Filter the song list as user types
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Listen for Enter key and hide keyboard
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });
    }

    // Function to filter songs dynamically
    private void filterSongs(String query) {
        if (query.trim().isEmpty()) {
            // Reset to full song list when input is cleared
            songAdapter.updateList(new ArrayList<>(songList));
            return;
        }

        List<Song> filteredList = new ArrayList<>();
        for (Song song : songList) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    song.getArtist().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(song);
            }
        }

        songAdapter.updateList(filteredList);
    }

    // Function to hide keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Sample songs for testing
    private List<Song> getDummySongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Blinding Lights", "The Weeknd", R.drawable.song_example));
        songs.add(new Song("Watermelon Sugar", "Harry Styles", R.drawable.song_example));
        songs.add(new Song("Save Your Tears", "The Weeknd", R.drawable.song_example));
        return songs;
    }
}
