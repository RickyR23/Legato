package com.example.legatoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(splashScreen.this, userHome.class);
            } else {
                intent = new Intent(splashScreen.this, userLoginActivity.class);
            }

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2000);
    }
}
