package com.example.legatoapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legatoapp.ArtistSearchPopup;
import com.example.legatoapp.models.MusicData;
import com.example.legatoapp.R;
import com.example.legatoapp.models.Song;
import com.example.legatoapp.SongSearchPopup;
import com.github.dhaval2404.imagepicker.ImagePicker;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editBioText, editDisplayNameText;
    private TextView bioCharCount, editProfilePicButton;
    private Button saveChangesButton;
    private static final int MAX_CHAR_COUNT = 50;
    private CircleImageView editProfilePicImageView;
    private Uri selectedImageUri;
    private Song selectedSong1, selectedSong2, selectedSong3;
    private String selectedArtist1, selectedArtist2, selectedArtist3;
    private int selectedArtist1Image, selectedArtist2Image, selectedArtist3Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editBioText = findViewById(R.id.editBioEditText);
        bioCharCount = findViewById(R.id.bioCharCount);
        editDisplayNameText = findViewById(R.id.editDisplayNameEditText);
        saveChangesButton = findViewById(R.id.saveButton);
        editProfilePicImageView = findViewById(R.id.editProfilePicImageView);
        editProfilePicButton = findViewById(R.id.uploadProfilePicButton);

        // Retrieve saved display name, bio and profile pic from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("saved_display_name", "");
        String bio = sharedPreferences.getString("saved_bio", "");
        String profilePic = sharedPreferences.getString("saved_profile_pic",null);

        if (!displayName.isEmpty()) {
            editDisplayNameText.setText(displayName);
        }

        if (!bio.isEmpty()) {
            editBioText.setText(bio);
        }

        if (profilePic != null) {
            Uri imageUri = Uri.parse(profilePic);
            editProfilePicImageView.setImageURI(imageUri);
        }

        // When Edit Profile Picture is clicked, launch image picker
        editProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

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

    // ImagePicker used to upload or take a profile picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Get the selected image URI
            selectedImageUri = data.getData();

            // Set the image URI to the profile picture
            editProfilePicImageView.setImageURI(selectedImageUri);
        }
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


            // Temporarily store selected song in local variables, to save later

            if (cardView.getId() == R.id.songCard1) {
                selectedSong1 = selectedSong;
            } else if (cardView.getId() == R.id.songCard2) {
                selectedSong2 = selectedSong;
            } else if (cardView.getId() == R.id.songCard3) {
                selectedSong3 = selectedSong;
            }
        }
    }

    // Updates artist card UI after selection
    private void updateArtistCard(View cardView, String artistName) {
        TextView artistView = cardView.findViewById(R.id.artistName);
        ImageView artistImageView = cardView.findViewById(R.id.artistImage);

        if (artistView != null && artistImageView != null) {
            artistView.setText(artistName);

            int artistImageRes = MusicData.getArtistImage(artistName);
            artistImageView.setImageResource(artistImageRes);

            // Temporarily store selected artist in local variables, to save later
            if (cardView.getId() == R.id.artistCard1) {
                selectedArtist1 = artistName;
                selectedArtist1Image = artistImageRes;
            } else if (cardView.getId() == R.id.artistCard2) {
                selectedArtist2 = artistName;
                selectedArtist2Image = artistImageRes;
            } else if (cardView.getId() == R.id.artistCard3) {
                selectedArtist3 = artistName;
                selectedArtist3Image = artistImageRes;
            }
        }
    }

    // Saves profile changes using SharedPreferences
    private void saveProfileChanges() {
        String displayName = editDisplayNameText.getText().toString().trim();
        String bio = editBioText.getText().toString().trim();

        if (displayName.isEmpty()) {
            editDisplayNameText.setError("Display name cannot be empty");
            editDisplayNameText.requestFocus();
            return; // Stop the save process if display name is empty
        }

        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Save display name and bio
        editor.putString("saved_display_name", displayName);
        editor.putString("saved_bio", bio);

        //Saves profile image URI if available
        if (selectedImageUri != null) {
            editor.putString("saved_profile_pic", selectedImageUri.toString());
        }

        // Save the selected songs if they were changed
        if (selectedSong1 != null) {
            editor.putString("saved_song_1", selectedSong1.getTitle());
            editor.putString("saved_song_1_artist", selectedSong1.getArtist());
            editor.putInt("saved_song_1_image", selectedSong1.getAlbumArt());
        }
        if (selectedSong2 != null) {
            editor.putString("saved_song_2", selectedSong2.getTitle());
            editor.putString("saved_song_2_artist", selectedSong2.getArtist());
            editor.putInt("saved_song_2_image", selectedSong2.getAlbumArt());
        }
        if (selectedSong3 != null) {
            editor.putString("saved_song_3", selectedSong3.getTitle());
            editor.putString("saved_song_3_artist", selectedSong3.getArtist());
            editor.putInt("saved_song_3_image", selectedSong3.getAlbumArt());
        }

        // Save the selected artists if they were changed
        if (selectedArtist1 != null) {
            editor.putString("saved_artist_1", selectedArtist1);
            editor.putInt("saved_artist_1_image", selectedArtist1Image);
        }
        if (selectedArtist2 != null) {
            editor.putString("saved_artist_2", selectedArtist2);
            editor.putInt("saved_artist_2_image", selectedArtist2Image);
        }
        if (selectedArtist3 != null) {
            editor.putString("saved_artist_3", selectedArtist3);
            editor.putInt("saved_artist_3_image", selectedArtist3Image);
        }

        // Apply changes
        editor.apply();
        finish();
    }
}
