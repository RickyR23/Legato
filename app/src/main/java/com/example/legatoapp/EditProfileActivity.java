package com.example.legatoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;
import android.widget.TextView;


public class EditProfileActivity extends AppCompatActivity {
    private EditText editBioText, editDisplayNameText;
    private TextView bioCharCount, displayNameTextView;
    private static final int MAX_CHAR_COUNT = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editBioText = findViewById(R.id.editBioEditText); // To edit the bio
        bioCharCount = findViewById(R.id.bioCharCount);  // To keep track of char count
        editDisplayNameText = findViewById(R.id.editDisplayNameEditText); // To edit display name

        // Retrieve the saved displayName from SharedPreferences saved from sign up
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("saved_display_name", "Enter your display name");

        // Set the displayName in the EditText
        if (!displayName.isEmpty()) {
            editDisplayNameText.setText(displayName);
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
    }
}
