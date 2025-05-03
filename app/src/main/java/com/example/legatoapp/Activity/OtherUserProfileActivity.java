package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.legatoapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class OtherUserProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);

        // Get data from Intent
        String displayName = getIntent().getStringExtra("displayName");
        String username = getIntent().getStringExtra("username");

        // Bind data to views
        TextView nameTextView = findViewById(R.id.textViewDisplayName);
        TextView bioTextView = findViewById(R.id.textViewBio);

        nameTextView.setText(displayName);
        bioTextView.setText("@" + username); // Placeholder if no bio

        // Back button logic
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}
