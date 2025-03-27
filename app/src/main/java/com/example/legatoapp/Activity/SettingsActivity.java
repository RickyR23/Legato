package com.example.legatoapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legatoapp.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Back to Profile Button
        ImageButton backToProfilePage = findViewById(R.id.backToProfilePage);
        backToProfilePage.setOnClickListener(view -> finish());

        // Logout Button Logic
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    // Logout User
    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.clear();  // This will remove all stored data in SharedPreferences
        editor.apply();

        Intent intent = new Intent(SettingsActivity.this, userLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
