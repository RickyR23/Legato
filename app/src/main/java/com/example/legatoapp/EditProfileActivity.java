package com.example.legatoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class EditProfileActivity extends AppCompatActivity {
    private EditText editBioText, editDisplayNameText;
    private TextView bioCharCount;
    private Button saveChangesButton;
    private static final int MAX_CHAR_COUNT = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editBioText = findViewById(R.id.editBioEditText); // To edit the bio
        bioCharCount = findViewById(R.id.bioCharCount);  // To keep track of char count
        editDisplayNameText = findViewById(R.id.editDisplayNameEditText); // To edit display name
        saveChangesButton = findViewById(R.id.saveButton); // To save changes made

        // Retrieve the saved displayName from SharedPreferences saved from sign up
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("saved_display_name", "Enter your display name");
        String bio = sharedPreferences.getString("saved_bio", "");

        // Set the displayName in the EditText
        if (!displayName.isEmpty()) {
            editDisplayNameText.setText(displayName);
        }

        // Set the bio in the EditText
        if (!bio.isEmpty()) {
            editBioText.setText(bio);
        }

        // TextWatcher checks that the bio updates character count
        editBioText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.length();
                bioCharCount.setText(currentLength + "/" + MAX_CHAR_COUNT);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Inside your Save button's OnClickListener
        saveChangesButton.setOnClickListener(v -> {
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Save Changes")
                    .setMessage("Do you want to save the changes made?")
                    .setPositiveButton("Save", (dialog, which) -> {
                        // Call save logic here to update the profile
                        saveProfileChanges();
                    })
                    .setNegativeButton("Discard", (dialog, which) -> {
                        // Discard the changes and close the dialog
                        dialog.dismiss();
                        finish();
                    })
                    .show();
        });

    }

    //****METHODS****//
    // This will save any changes made in the Edit Profile activity
    private void saveProfileChanges() {
        // Save changes logic
        // At the moment we are using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_display_name", editDisplayNameText.getText().toString().trim());
        editor.putString("saved_bio", editBioText.getText().toString().trim());
        editor.apply(); // Save the data

        finish(); //Finish activity
    }

}
