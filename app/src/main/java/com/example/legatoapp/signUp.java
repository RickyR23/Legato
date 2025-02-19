package com.example.legatoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Pattern;
import android.util.Patterns;


public class signUp extends AppCompatActivity {

    private EditText passwordInput, verifyPasswordInput, usernameInput, displayNameInput, emailInput;
    private TextView passwordErrorMsg, passwordMismatchMsg, usernameErrorMsg, displayNameErrorMsg, emailErrorMsg;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // UI Elements
        ImageButton backToLogInButton = findViewById(R.id.backToLogInButton);
        passwordInput = findViewById(R.id.editTextPassword);
        verifyPasswordInput = findViewById(R.id.editTextVerifyPassword);
        usernameInput = findViewById(R.id.editTextUsername);
        displayNameInput = findViewById(R.id.editTextDisplayName);
        emailInput = findViewById(R.id.editTextEmail);
        passwordErrorMsg = findViewById(R.id.passwordErrorText);
        passwordMismatchMsg = findViewById(R.id.passwordMismatchText);
        usernameErrorMsg = findViewById(R.id.usernameErrorText);
        displayNameErrorMsg = findViewById(R.id.displayNameErrorText);
        emailErrorMsg = findViewById(R.id.emailErrorText);
        createAccountButton = findViewById(R.id.buttonCreateAccount);

        // Initially disable 'Create Account' button
        createAccountButton.setEnabled(false);

        // FocusChange to validate display name
        displayNameInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // Validate when user moves to another field
                String displayName = displayNameInput.getText().toString().trim();
                if (isValidDisplayName(displayName)) {
                    displayNameErrorMsg.setVisibility(View.GONE);
                } else {
                    displayNameErrorMsg.setVisibility(View.VISIBLE);
                }
            }
        });

        // FocusChange to validate email
        emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // Validate when user moves to another field
                String email = emailInput.getText().toString().trim();
                if (isValidEmail(email)) {
                    emailErrorMsg.setVisibility(View.GONE);
                } else {
                    emailErrorMsg.setVisibility(View.VISIBLE);
                }
            }
        });

        // TextWatcher to validate username
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Trim the username to remove extra spaces before or after it is entered
                String username = usernameInput.getText().toString().trim();

                // Check if username meets criteria, otherwise display message
                if (isValidUsername(username)) {
                    usernameErrorMsg.setVisibility(View.GONE);
                } else {
                    usernameErrorMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        // TextWatcher used to validate password and verifyPassword
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Trim the password and verifypassword to remove extra spaces before or after it is entered
                String password = passwordInput.getText().toString().trim();
                String verifyPassword = verifyPasswordInput.getText().toString().trim();

                // Validate if password meets criteria otherwise display message
                if (isValidPassword(password)) {
                    passwordErrorMsg.setVisibility(View.GONE);
                } else {
                    passwordErrorMsg.setVisibility(View.VISIBLE);
                }

                // Check if password match in both fields otherwise display message
                if (!verifyPassword.isEmpty() && !password.equals(verifyPassword)) {
                    passwordMismatchMsg.setVisibility(View.VISIBLE);
                } else {
                    passwordMismatchMsg.setVisibility(View.GONE);
                }

                // Enable 'Create Account' button only if both conditions are met
                createAccountButton.setEnabled(isValidPassword(password) && password.equals(verifyPassword));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Attach the TextWatcher to both password fields
        passwordInput.addTextChangedListener(passwordWatcher);
        verifyPasswordInput.addTextChangedListener(passwordWatcher);

        // ClickListener to go back to login screen
        backToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, userLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    } // End of onCreate

    /*-----------VALIDATION METHODS-----------*/
    // Username validation function
    private boolean isValidUsername(String username) {
        /* Regex pattern is used to check username criteria is met
            [a-zA-Z0-9] - Just numbers and letters (lowercase and/or uppercase)
            {4-10} - Length between 4 and 10 characters
         */
        String usernamePattern = "^[a-zA-Z0-9]{4,10}$";
        return Pattern.matches(usernamePattern, username);
    }

    // Password validation function
    private boolean isValidPassword(String password) {
        /* Regex pattern is used to check password criteria is met
            (?=.*[a-z]) - At least one lowercase
            (?=.*[A-Z]) - At least one uppercase
            (?=.*\d) - At least one number
            (?=.*[@$!%*?&]) - At least one special character
            {5-16} - Length between 5 and 16 characters
         */
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,16}$";
        return Pattern.matches(passwordPattern, password);
    }

    // Display Name validation function
    private boolean isValidDisplayName(String displayName) {
        // True if displayName is not empty and between 4-25 in length
        return !displayName.isEmpty() && displayName.length() >= 4 && displayName.length() <= 25;
    }

    // Email validation function
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}