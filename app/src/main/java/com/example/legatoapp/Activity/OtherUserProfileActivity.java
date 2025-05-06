package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legatoapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class OtherUserProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);

        // ToDo: Implement the logic to retrieve the rest of the data from db like top songs, artists, bio, followers, following, etc

        // Get data from Intent
        String username = getIntent().getStringExtra("USERNAME");
        String displayName = getIntent().getStringExtra("DISPLAY_NAME");
        int profileImageResId = getIntent().getIntExtra("PROFILE_IMAGE_RES_ID", R.drawable.profile_pic_placeholder);

        // Bind data
        TextView displayNameText = findViewById(R.id.textViewDisplayName);
        ImageView profileImage = findViewById(R.id.profilePicImageView);

        displayNameText.setText(displayName);
        profileImage.setImageResource(profileImageResId);

        // Back button logic
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}
