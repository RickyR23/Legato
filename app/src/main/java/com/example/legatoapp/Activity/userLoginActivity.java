package com.example.legatoapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legatoapp.R;
import com.example.legatoapp.databinding.ActivityUserLoginBinding;

public class userLoginActivity extends AppCompatActivity {

    private ActivityUserLoginBinding binding;
    private String userInputString;
    private String passwordInputString;
    private boolean isPasswordVisible = false; // Track password visibility state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            launchHomeActivity();
            finish();
            return;
        }

        EditText passwordInput = findViewById(R.id.loginPassword);
        EditText usernameInput = findViewById(R.id.username);
        ImageView togglePassword = findViewById(R.id.togglePassword);
        Button loginButton = findViewById(R.id.launch_tohomepage);
        Button signUpButton = findViewById(R.id.launch_tosignuppage);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText); // Fix: Find Forgot Password TextView

        // Handle password visibility toggle
        togglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cursorPosition = passwordInput.getSelectionStart();
                Typeface typeface = passwordInput.getTypeface();

                if (isPasswordVisible) {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePassword.setImageResource(R.drawable.eye_fill);
                } else {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePassword.setImageResource(R.drawable.eye_slash);
                }

                passwordInput.setTypeface(typeface);
                passwordInput.setSelection(cursorPosition);
                isPasswordVisible = !isPasswordVisible;
            }
        });

        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInputString = usernameInput.getText().toString().trim();
                passwordInputString = passwordInput.getText().toString().trim();

                if (!validateInputs(userInputString, passwordInputString)) {
                    return;
                }

                boolean checkLoginValidity = checkLogin(userInputString, passwordInputString);
                Log.d("checkLoginValidity", "is true?: " + checkLoginValidity);

                if (checkLoginValidity) {
                    saveLoginState();
                    launchHomeActivity();
                    finish();
                } else {
                    Toast.makeText(userLoginActivity.this, "Invalid credentials. Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Handle sign-up button click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userLoginActivity.this, signUp.class);
                startActivity(intent);
            }
        });

        // Handle forgot password text click (Fixed issue)
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userLoginActivity.this, forgetPassword.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkLogin(String username, String password) {
        String mockUsername = "admin";
        String mockPassword = "password";

        return username.equals(mockUsername) && password.equals(mockPassword);
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(userLoginActivity.this, userHome.class);
        startActivity(intent);
    }

    private void saveLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }
}
