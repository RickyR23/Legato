package com.example.legatoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.example.legatoapp.R;

public class forgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Find the back to login button by its ID
        ImageButton backToLogInButton = findViewById(R.id.backToLogInButton);

        // Set an OnClickListener on the back to login button
        backToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the LoginActivity
                Intent intent = new Intent(forgetPassword.this, userLoginActivity.class);

                // Start the LoginActivity
                startActivity(intent);

                // Finish the current activity to remove it from the back stack
                finish();
            }
        });
    }
}