package com.example.legatoapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import java.util.regex.Pattern;
import android.util.Patterns;


import com.example.legatoapp.CognitoAuth;
import com.example.legatoapp.R;
import com.example.legatoapp.Services.helper.SpotifyAuthHelper;


import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;


public class signUp extends AppCompatActivity {

    private EditText passwordInput, verifyPasswordInput, usernameInput, displayNameInput, emailInput;
    private TextView passwordErrorMsg, passwordMismatchMsg, usernameErrorMsg, displayNameErrorMsg, emailErrorMsg;
    private Button createAccountButton, verificationButton;
    boolean isPasswordVisible = false;
    boolean isVerifyPasswordVisible = false;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        CognitoAuth.initialize(getApplicationContext());
        Uri receivedUri = getIntent().getData();
        if (receivedUri == null || !receivedUri.toString().startsWith(getString(R.string.REDIRECT_URI))) {
            clearSavedFormData();
        }


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
        verificationButton = findViewById(R.id.show_verificaiton_popup);

        // We want to make this button gone for the case of the user data not currently being obtained on activity startup, we will only display this IF the user has already entered their data to be sent to cognito
        verificationButton.setVisibility(View.GONE);

// UI Elements for toggling password visibility
        ImageView togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        ImageView toggleVerifyPasswordVisibility = findViewById(R.id.toggleVerifyPasswordVisibility);

// Toggle Password Visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            int cursorPosition = passwordInput.getSelectionStart();
            Typeface typeface = passwordInput.getTypeface();
            if (isPasswordVisible) {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.eye_fill);
            } else {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.eye_slash);
            }
            passwordInput.setTypeface(typeface);
            passwordInput.setSelection(cursorPosition);
            isPasswordVisible = !isPasswordVisible;
        });

// Toggle Verify Password Visibility
        toggleVerifyPasswordVisibility.setOnClickListener(v -> {
            int cursorPosition = verifyPasswordInput.getSelectionStart();
            Typeface typeface = verifyPasswordInput.getTypeface();
            if (isVerifyPasswordVisible) {
                verifyPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleVerifyPasswordVisibility.setImageResource(R.drawable.eye_fill);
            } else {
                verifyPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleVerifyPasswordVisibility.setImageResource(R.drawable.eye_slash);
            }
            verifyPasswordInput.setTypeface(typeface);
            verifyPasswordInput.setSelection(cursorPosition);
            isVerifyPasswordVisible = !isVerifyPasswordVisible;
        });


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String username = usernameInput.getText().toString().trim();
                String displayName = displayNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String verifyPassword = verifyPasswordInput.getText().toString().trim();

                // Validate inputs
                if (!isValidUsername(username) || !isValidPassword(password) || !password.equals(verifyPassword)
                        || !isValidEmail(email) || !isValidDisplayName(displayName)) {
                    return;
                }

                try {
                    Log.d("Signup", "Entered Try for Signup");

                    // Handles User Signup
                    CognitoAuth.signUpUser(username, displayName, email, password, new CognitoAuth.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Signup", "Entered OnSuccess");
                            runOnUiThread(() -> showVerificationPopup(username));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e("SignUpError", "Signup failed: " + errorMessage);
                        }
                    });
                } catch (CognitoIdentityProviderException e) {
                    Log.e("SignUpError", "Signup failed: " + e.awsErrorDetails().errorMessage());
                }
            }
        });

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVerificationPopup("");
            }
        });


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


        Button connectSpotifyButton = findViewById(R.id.buttonConnectSpotify);
        connectSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSpotifyAuthSession();
            }
        });


        // Inside onCreate()
        createAccountButton.setAlpha(0.5f); // Set transparency at the start
        createAccountButton.setEnabled(false); // Disable initially


        TextWatcher formWatcher = new TextWatcher() {
            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Get input values
                String username = usernameInput.getText().toString().trim();
                String displayName = displayNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String verifyPassword = verifyPasswordInput.getText().toString().trim();

                // Check if all fields are valid
                boolean isValidForm = isValidUsername(username) &&
                        isValidDisplayName(displayName) &&
                        isValidEmail(email) &&
                        isValidPassword(password) &&
                        password.equals(verifyPassword);

                // Check if Spotify tokens are received
                boolean isSpotifyTokenReceived = checkSpotifyTokenReceived();

                // Enable button only when both conditions are met
                if (isValidForm && isSpotifyTokenReceived) {
                    createAccountButton.setAlpha(1.0f); // Fully visible
                    createAccountButton.setEnabled(true);
                } else {
                    createAccountButton.setAlpha(0.5f); // Semi-transparent
                    createAccountButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

// Attach watcher to form fields
        usernameInput.addTextChangedListener(formWatcher);
        displayNameInput.addTextChangedListener(formWatcher);
        emailInput.addTextChangedListener(formWatcher);
        passwordInput.addTextChangedListener(formWatcher);
        verifyPasswordInput.addTextChangedListener(formWatcher);


    }
    private boolean checkSpotifyTokenReceived() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isSpotifyTokenReceived", false);
    }



    @Override
    protected void onResume() {
        super.onResume();

        restoreFormData(); // Restore user input if sign-up is incomplete

        // Check if we received the Spotify authentication response
        Uri receivedUri = getIntent().getData();
        if (receivedUri != null && receivedUri.toString().startsWith(context.getString(R.string.REDIRECT_URI))) {
            String authCode = receivedUri.getQueryParameter("code");
            if (authCode != null) {
                exchangeAuthorizationForToken(authCode);
            }
        }

        validateFormAndToggleButton();
    }
    // End of onCreate

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    private void launchSpotifyAuthSession(){
        saveFormData(); // Save user input before leaving

        Uri authenticationURI = new Uri.Builder()
                .scheme("https")
                .authority("accounts.spotify.com")
                .appendPath("authorize")
                .appendQueryParameter("client_id",context.getString(R.string.CLIENT_ID))
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("redirect_uri", context.getString(R.string.REDIRECT_URI))
                .appendQueryParameter("scope", context.getString(R.string.SPOTIFY_SCOPES))
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, authenticationURI);
        startActivity(intent);
    }

    private void exchangeAuthorizationForToken(String code){
        Disposable disposable = SpotifyAuthHelper.fetchAccessToken(context, code)
                .subscribe(response -> {
                    SpotifyAuthHelper.storeSpotifyTokens(this, response.getAccess_token(), response.getRefresh_token());
                    Log.d("SpotifyAuthService", "Spotify has received token response: \n Access Token: " + response.getAccess_token() + "\n refreshToken: " + response.getRefresh_token());
                }, error -> Log.e("SpotifyAuthService", "Error has occured . . . : " + error.getMessage()));
        compositeDisposable.add(disposable);

        runOnUiThread(() -> {
            Button connectSpotifyButton = findViewById(R.id.buttonConnectSpotify);
            connectSpotifyButton.setAlpha(0.5f);
            connectSpotifyButton.setText(R.string.Spotify_Conncted);
            connectSpotifyButton.setEnabled(false);
        });
    }
    private void saveFormData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("saved_username", usernameInput.getText().toString().trim());
        editor.putString("saved_display_name", displayNameInput.getText().toString().trim());
        editor.putString("saved_email", emailInput.getText().toString().trim());
        editor.putString("saved_password", passwordInput.getText().toString().trim());
        editor.putString("saved_verify_password", verifyPasswordInput.getText().toString().trim());

        editor.apply(); // Save the data
    }

    private void restoreFormData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);

        usernameInput.setText(sharedPreferences.getString("saved_username", ""));
        displayNameInput.setText(sharedPreferences.getString("saved_display_name", ""));
        emailInput.setText(sharedPreferences.getString("saved_email", ""));
        passwordInput.setText(sharedPreferences.getString("saved_password", ""));
        verifyPasswordInput.setText(sharedPreferences.getString("saved_verify_password", ""));
    }

    private void clearFormData() {
        usernameInput.setText("");
        displayNameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        verifyPasswordInput.setText("");
    }

    private void clearSavedFormData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("saved_username");
        editor.remove("saved_display_name");
        editor.remove("saved_email");
        editor.remove("saved_password");
        editor.remove("saved_verify_password");
        editor.apply();
    }


    private void validateFormAndToggleButton() {
        String username = usernameInput.getText().toString().trim();
        String displayName = displayNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String verifyPassword = verifyPasswordInput.getText().toString().trim();

        boolean isValidForm = isValidUsername(username) &&
                isValidDisplayName(displayName) &&
                isValidEmail(email) &&
                isValidPassword(password) &&
                password.equals(verifyPassword);

        boolean isSpotifyTokenReceived = checkSpotifyTokenReceived();

        if (isValidForm && isSpotifyTokenReceived) {
            createAccountButton.setAlpha(1.0f); // Fully visible
            createAccountButton.setEnabled(true);
        } else {
            createAccountButton.setAlpha(0.5f); // Semi-transparent
            createAccountButton.setEnabled(false);
        }
    }

    private void showVerificationPopup(String username){
        View popupInstance = getLayoutInflater().inflate(R.layout.popup_signup_confirmation_code, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupInstance);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // currently testing to see wth this does . . . for edge cases . . .

        ImageButton exitBtn = popupInstance.findViewById(R.id.popup_exit_button);
        EditText verificationCodeInput = popupInstance.findViewById(R.id.popup_text_input);
        Button verifyBtn = popupInstance.findViewById(R.id.popup_verify_button);

        verificationButton = findViewById(R.id.show_verificaiton_popup);


        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                verificationButton.setVisibility(View.VISIBLE);
            }
        });


        verifyBtn.setOnClickListener(view -> {
            String verificationCodeText = verificationCodeInput.getText().toString().trim();

            if (verificationCodeText.isEmpty()) {
                Log.d("showVerificationPopup", "Empty string in verification code . . . ");
            } else {

                CognitoAuth.confirmUser(username, verificationCodeText, new CognitoAuth.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("SignUpSuccess", "User confirmed successfully!");


                        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        dialog.dismiss();
                        saveFormData();


                        Intent intent = new Intent(signUp.this, userHome.class);
                        startActivity(intent);
                        finish();


                        clearFormData();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("SignUpError", "Signup confirmation failed: " + errorMessage);
                    }
                });
            }
        });


        dialog.show();
    }

}