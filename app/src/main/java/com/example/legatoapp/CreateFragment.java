package com.example.legatoapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

public class CreateFragment extends Fragment {

    private ImageView selectedSongImage, testArtistImage;
    private TextView selectedSongName, selectedArtistName, testArtistSearchResult;
    private EditText captionInput;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        // Initialize UI components
        selectedSongImage = view.findViewById(R.id.selected_song_image);
        selectedSongName = view.findViewById(R.id.selected_song_name);
        selectedArtistName = view.findViewById(R.id.selected_artist_name);
        testArtistImage = view.findViewById(R.id.test_artist_image);
        testArtistSearchResult = view.findViewById(R.id.test_artist_search_result);
        captionInput = view.findViewById(R.id.caption_input);

        // Song Search Button
        Button searchButton = view.findViewById(R.id.btn_open_search);
        searchButton.setOnClickListener(v -> {
            SongSearchPopup songSearchPopup = new SongSearchPopup(getContext(), this::updateSelectedSong);
            songSearchPopup.showPopup(v);
        });

        // Artist Search Button
        Button artistSearchButton = view.findViewById(R.id.btn_open_artist_search);
        artistSearchButton.setOnClickListener(v -> {
            ArtistSearchPopup artistSearchPopup = new ArtistSearchPopup(getContext(), this::updateSelectedArtist);
            artistSearchPopup.showPopup(v);
        });

        // Hide keyboard when pressing Enter in the caption input
        captionInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboard(v);
                return true;
            }
            return false;
        });

        return view;
    }

    // Updates the UI when a song is selected
    public void updateSelectedSong(Song song) {
        selectedSongImage.setImageResource(song.getAlbumArt());
        selectedSongName.setText(song.getTitle());
        selectedArtistName.setText(song.getArtist());
    }

    // Updates the UI when an artist is selected
    public void updateSelectedArtist(String artist) {
        testArtistSearchResult.setText("Selected Artist: " + artist);

        // Fetch correct artist image from MusicData
        testArtistImage.setImageResource(MusicData.getArtistImage(artist));
    }

    // Function to hide the keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
