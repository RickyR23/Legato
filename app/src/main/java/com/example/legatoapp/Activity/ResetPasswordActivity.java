package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.legatoapp.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText emailEntered;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // BACK TO SETTINGS BUTTON
        ImageButton backToSettings = findViewById(R.id.backToSettings);
        backToSettings.setOnClickListener(v -> {
            // Checks for unsaved changes before returning to settings
            checkForUnsavedChanges();
        });

        emailEntered = findViewById(R.id.emailInput);
        Button resetPassButton = findViewById(R.id.resetPasswordButton);

        // Handle RESET PASSWORD BUTTON action
        resetPassButton.setOnClickListener(view -> {
            //Obtain email entered
            String emailInput = emailEntered.getText().toString().trim();

            // If email field is empty or email is not valid
            if (emailInput.isEmpty() || !isValidEmail(emailInput)) {
                Toast.makeText(ResetPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();

            } else {
                // Show success message
                Toast.makeText(ResetPasswordActivity.this, "An email has been sent to " + emailInput, Toast.LENGTH_SHORT).show();

                // ToDo : Here is where the logic for resetting the password can go
                //  it can go to another page to enter a verification code or such
                // For now, when the button is clicked it ends the activity and returns to Settings
                finish();
            }
        });


    }

    /*-------------METHODS-------------*/
    // Checks if there are unsaved changes before returning to settings, if so display confirmation message
    private void checkForUnsavedChanges() {
        String emailInput = emailEntered.getText().toString().trim();

        // If there are unsaved changes, show a confirmation dialog
        if (!emailInput.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("Are you sure you want to leave?")
                    .setCancelable(false) //Prevent dismiss by tapping outside the dialog box
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Navigate back to Settings if the user confirms selection
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            // No unsaved changes, just go back to settings
            finish();
        }
    }

    // Checks that the email is valid
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        // ToDo : Here it can check if the email entered is registered to said account
    }
}
