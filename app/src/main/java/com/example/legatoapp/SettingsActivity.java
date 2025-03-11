package com.example.legatoapp;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find the back to profile button by its ID
        ImageButton backToProfilePage = findViewById(R.id.backToProfilePage);

        backToProfilePage.setOnClickListener(view -> {
            finish(); // This will close SettingsActivity and return to the previous screen (ProfileFragment)
        });
    }
}
