package com.example.legatoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editBioText, editDisplayNameText;
    private TextView bioCharCount;
    private Button saveChangesButton;
    private static final int MAX_CHAR_COUNT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editBioText = findViewById(R.id.editBioEditText);
        bioCharCount = findViewById(R.id.bioCharCount);
        editDisplayNameText = findViewById(R.id.editDisplayNameEditText);
        saveChangesButton = findViewById(R.id.saveButton);

        // Retrieve saved display name & bio from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("saved_display_name", "");
        String bio = sharedPreferences.getString("saved_bio", "");

        if (!displayName.isEmpty()) {
            editDisplayNameText.setText(displayName);
        }

        if (!bio.isEmpty()) {
            editBioText.setText(bio);
        }

        // Character count update for bio
        editBioText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                bioCharCount.setText(currentLength + "/" + MAX_CHAR_COUNT);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Save changes button
        saveChangesButton.setOnClickListener(v -> {
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Save Changes")
                    .setMessage("Do you want to save the changes made?")
                    .setPositiveButton("Save", (dialog, which) -> saveProfileChanges())
                    .setNegativeButton("Discard", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    })
                    .show();
        });

        // Set up song card click listeners
        setupSongCardClickListener(R.id.songCard1);
        setupSongCardClickListener(R.id.songCard2);
        setupSongCardClickListener(R.id.songCard3);

        // Set up artist card click listeners
        setupArtistCardClickListener(R.id.artistCard1);
        setupArtistCardClickListener(R.id.artistCard2);
        setupArtistCardClickListener(R.id.artistCard3);
    }

    // Click listener for song cards
    private void setupSongCardClickListener(int cardId) {
        View songCard = findViewById(cardId);
        if (songCard != null) {
            songCard.setOnClickListener(v -> {
                SongSearchPopup popup = new SongSearchPopup(this, selectedSong -> {
                    updateSongCard(songCard, selectedSong);
                });
                popup.showPopup(songCard);
            });
        }
    }

    // Click listener for artist cards
    private void setupArtistCardClickListener(int cardId) {
        View artistCard = findViewById(cardId);
        if (artistCard != null) {
            artistCard.setOnClickListener(v -> {
                ArtistSearchPopup popup = new ArtistSearchPopup(this, selectedArtist -> {
                    updateArtistCard(artistCard, selectedArtist);
                });
                popup.showPopup(artistCard);
            });
        }
    }

    // Updates song card UI after selection
    private void updateSongCard(View cardView, Song selectedSong) {
        TextView titleView = cardView.findViewById(R.id.songTitle);
        TextView artistView = cardView.findViewById(R.id.songArtist);
        ImageView albumArtView = cardView.findViewById(R.id.songImage);

        if (titleView != null && artistView != null && albumArtView != null) {
            titleView.setText(selectedSong.getTitle());
            artistView.setText(selectedSong.getArtist());
            albumArtView.setImageResource(selectedSong.getAlbumArt());


            //Save song to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Identify the correct card and save accordingly
            //Save card 1
            if (cardView.getId() == R.id.songCard1) {
                editor.putString("saved_song_1", selectedSong.getTitle());
                editor.putString("saved_song_1_artist", selectedSong.getArtist());
            }
            //Save card 2
            else if (cardView.getId() == R.id.songCard2) {
                editor.putString("saved_song_2", selectedSong.getTitle());
                editor.putString("saved_song_2_artist", selectedSong.getArtist());
            }
            //Save card 3
            else if (cardView.getId() == R.id.songCard3) {
                editor.putString("saved_song_3", selectedSong.getTitle());
                editor.putString("saved_song_3_artist", selectedSong.getArtist());
            }
            editor.apply();
        }
    }

    // Updates artist card UI after selection
    private void updateArtistCard(View cardView, String artistName) {
        TextView artistView = cardView.findViewById(R.id.artistName);
        ImageView artistImageView = cardView.findViewById(R.id.artistImage);

        if (artistView != null && artistImageView != null) {
            artistView.setText(artistName);
            artistImageView.setImageResource(MusicData.getArtistImage(artistName));

            //Save artist to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Identify the correct card and save accordingly
            //Save card 1
            if (cardView.getId() == R.id.artistCard1) {
                editor.putString("saved_artist_1", artistName);
            }
            //Save card 2
            else if (cardView.getId() == R.id.artistCard2) {
                editor.putString("saved_artist_2", artistName);
            }
            //Save card 3
            else if (cardView.getId() == R.id.artistCard3) {
                editor.putString("saved_artist_3", artistName);
            }
            editor.apply();
        }
    }

    // Saves profile changes using SharedPreferences
    private void saveProfileChanges() {
        String displayName = editDisplayNameText.getText().toString().trim();
        String bio = editBioText.getText().toString().trim();

        if (displayName.isEmpty()) {
            editDisplayNameText.setError("Display name cannot be empty");
            editDisplayNameText.requestFocus();
            return; // Stop the save process
        }

        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_display_name", displayName);
        editor.putString("saved_bio", bio);
        editor.apply();
        finish();
    }
}
