package com.example.legatoapp.Activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.provider.Settings;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.legatoapp.R;

public class SettingsActivity extends AppCompatActivity {
    private ToggleButton notificationsButton, nightThemeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // BACK TO PROFILE BUTTON
        ImageButton backToProfilePage = findViewById(R.id.backToProfilePage);
        backToProfilePage.setOnClickListener(view -> finish());

        // LOGOUT BUTTON
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // NOTIFICATIONS TOGGLE BUTTON
        notificationsButton = findViewById(R.id.notificationsToggle);
        updateNotificationToggleState();

        // Acts when user tries to change the notification settings
        notificationsButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            boolean currentState = notificationManager.areNotificationsEnabled();

            if (isChecked != currentState) {
                Toast.makeText(this, "Redirecting to notification settings...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
        });

        // THEME TOGGLE BUTTON
        nightThemeButton = findViewById(R.id.nightThemeToggle);
        updateThemeToggleState();

        // Handle the toggle button change to change themes
        nightThemeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // Enable Light Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // REPORT BUTTON directs to Report Activity when clicked
        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        /*
        // RESET PASSWORD BUTTON directs to Reset Password Activity when clicked
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        }); */

        // FAQ BUTTON directs to FAQ Activity when clicked
        Button faqButton = findViewById(R.id.faqButton);
        faqButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, FAQActivity.class);
            startActivity(intent);
        });
    }

    // Refresh toggle when returning
    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationToggleState();
    }

    /*------------------METHODS------------------*/
    // Update the toggle button accordingly to the user selection in the system settings
    private void updateNotificationToggleState() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        boolean systemNotificationsEnabled = notificationManager.areNotificationsEnabled();
        notificationsButton.setChecked(systemNotificationsEnabled);
    }

    // Update the theme toggle button state based on current mode
    private void updateThemeToggleState() {
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        // Set the toggle button based on the current mode
        nightThemeButton.setChecked(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);
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
