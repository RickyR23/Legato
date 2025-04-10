package com.example.legatoapp.Fragment;

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

import com.example.legatoapp.R;
import com.example.legatoapp.SongSearchPopup;
import com.example.legatoapp.models.Song;

public class CreateFragment extends Fragment {

    private ImageView selectedSongImage;
    private TextView selectedSongName, selectedArtistName;
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
        captionInput = view.findViewById(R.id.caption_input);

        // Song Search Button
        Button searchButton = view.findViewById(R.id.btn_open_search);
        searchButton.setOnClickListener(v -> {
            SongSearchPopup songSearchPopup = new SongSearchPopup(getContext(), this::updateSelectedSong);
            songSearchPopup.showPopup(v);
        });

        // Hide keyboard when pressing Enter in the caption input
        captionInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboard(v);
                return true;
            }
            return false;
        });

        Button clearButton = view.findViewById(R.id.btn_clear);
        Button postButton = view.findViewById(R.id.btn_post);

// Clear button
        clearButton.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Clear Post")
                    .setMessage("Are you sure you want to clear everything?")
                    .setPositiveButton("Clear", (dialog, which) -> {
                        selectedSongImage.setImageResource(R.drawable.snoopypfp); // default image
                        selectedSongName.setText("Song Name");
                        selectedArtistName.setText("Song Artist");
                        captionInput.setText("");
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


// Post button
        postButton.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Post")
                    .setMessage("Are you sure you want to post this?")
                    .setPositiveButton("Post", (dialog, which) -> {
                        submitPost();
                        android.widget.Toast.makeText(getContext(), "Post submitted!", android.widget.Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }

    // Updates the UI when a song is selected
    public void updateSelectedSong(Song song) {
        selectedSongImage.setImageResource(song.getAlbumArt());
        selectedSongName.setText(song.getTitle());
        selectedArtistName.setText(song.getArtist()); // This updates the artist from the song
    }

    // Function to hide the keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void submitPost() {
        // LOGIC WILL GO HERE
        // Temporary confirmation
        android.widget.Toast.makeText(getContext(), "Post submitted!", android.widget.Toast.LENGTH_SHORT).show();
    }

    public boolean hasUnsavedChanges() {
        String caption = captionInput.getText().toString().trim();
        boolean isCaptionEntered = !caption.isEmpty();
        boolean isSongSelected = !selectedSongName.getText().toString().equals("Song Name") &&
                !selectedArtistName.getText().toString().equals("Song Artist");
        return isCaptionEntered || isSongSelected;
    }



}
