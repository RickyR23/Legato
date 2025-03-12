package com.example.legatoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.widget.EditText;
import android.widget.TextView;


public class EditProfileActivity extends AppCompatActivity {
    private EditText editBioEditText;
    private TextView bioCharCount;
    private static final int MAX_CHAR_COUNT = 80;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editBioEditText = findViewById(R.id.editBioEditText);
        bioCharCount = findViewById(R.id.bioCharCount);

        // TextWatcher checks that the bio updates character count
        editBioEditText.addTextChangedListener(new TextWatcher() {
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
