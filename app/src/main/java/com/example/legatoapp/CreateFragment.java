package com.example.legatoapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateFragment extends Fragment {

    private ImageView selectedSongImage;
    private EditText selectedSongName, selectedArtistName;

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

        Button searchButton = view.findViewById(R.id.btn_open_search);
        searchButton.setOnClickListener(v -> {
            SongSearchPopup songSearchPopup = new SongSearchPopup(getContext(), this::updateSelectedSong);
            songSearchPopup.showPopup(v);
        });

        return view;
    }

    // Updates the UI when a song is selected
    public void updateSelectedSong(Song song) {
        selectedSongImage.setImageResource(song.getAlbumArt());
        selectedSongName.setText(song.getTitle());
        selectedArtistName.setText(song.getArtist());
    }
}
