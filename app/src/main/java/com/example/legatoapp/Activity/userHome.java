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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class userHome extends AppCompatActivity {

    ActivityUserHomeBinding binding;
    private BottomNavigationView.OnItemSelectedListener navListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set Home Fragment by Default
        replaceFragment(new HomeFragment());

        // Define navigation listener
        navListener = item -> {
            int id = item.getItemId();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

            // Check if we are in CreateFragment and trying to leave
            if (currentFragment instanceof CreateFragment && id != R.id.create) {
                CreateFragment createFragment = (CreateFragment) currentFragment;
                if (createFragment.hasUnsavedChanges()) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("Leave Create Post?")
                            .setMessage("You have unsaved changes. Are you sure you want to leave this page?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                binding.bottomNavigationView.setOnItemSelectedListener(null); // Disable listener temporarily

                                if (id == R.id.home) {
                                    replaceFragment(new HomeFragment());
                                } else if (id == R.id.inbox) {
                                    replaceFragment(new InboxFragment());
                                } else if (id == R.id.profile) {
                                    replaceFragment(new ProfileFragment());
                                }

                                binding.bottomNavigationView.setSelectedItemId(id); // Sync tab visually
                                binding.bottomNavigationView.setOnItemSelectedListener(navListener); // Re-enable listener
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    return false;
                }
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
        };

        // Attach the listener
        binding.bottomNavigationView.setOnItemSelectedListener(navListener);
    }

    // Replace Fragment Method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
