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

public class signUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // UI Elements
        ImageButton backToLogInButton = findViewById(R.id.backToLogInButton);
        EditText passwordInput = findViewById(R.id.editTextPassword);
        EditText verifyPasswordInput = findViewById(R.id.editTextVerifyPassword);
        TextView passwordErrorMsg = findViewById(R.id.passwordErrorText);
        TextView passwordMismatchMsg = findViewById(R.id.passwordMismatchText);
        Button createAccountButton = findViewById(R.id.buttonCreateAccount);

        // Initially disable 'Create Account' button
        createAccountButton.setEnabled(false);

        // TextWatcher used to validate password and match
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = passwordInput.getText().toString();
                String verifyPassword = verifyPasswordInput.getText().toString();

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


        backToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, userLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
}