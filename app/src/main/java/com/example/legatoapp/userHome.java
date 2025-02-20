package com.example.legatoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.legatoapp.databinding.ActivityUserHomeBinding;

public class userHome extends AppCompatActivity {

    ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set Home Fragment by Default
        replaceFragment(new HomeFragment());

        // Logout Button Logic
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Bottom Navigation Click Listener (Fixed if-else)
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (id == R.id.create) {
                replaceFragment(new CreateFragment());
                return true;
            } else if (id == R.id.inbox) {
                replaceFragment(new InboxFragment());
                return true;
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
                return true;
            }

            return false;
        });
    }

    // Replace Fragment Method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Logout User
    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(userHome.this, userLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
