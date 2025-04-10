package com.example.legatoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.legatoapp.Fragment.CreateFragment;
import com.example.legatoapp.Fragment.HomeFragment;
import com.example.legatoapp.Fragment.InboxFragment;
import com.example.legatoapp.Fragment.ProfileFragment;
import com.example.legatoapp.R;
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

        // Bottom Navigation Click Listener with CreateFragment Exit Confirmation
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

            // If currently in CreateFragment and trying to leave to another fragment
            if (currentFragment instanceof CreateFragment && id != R.id.create) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Leave Create Post?")
                        .setMessage("You have unsaved changes. Are you sure you want to leave this page?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (id == R.id.home) {
                                replaceFragment(new HomeFragment());
                            } else if (id == R.id.inbox) {
                                replaceFragment(new InboxFragment());
                            } else if (id == R.id.profile) {
                                replaceFragment(new ProfileFragment());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return false;
            }

            // Normal behavior
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

}
