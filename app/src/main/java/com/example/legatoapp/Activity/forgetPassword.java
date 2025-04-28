package com.example.legatoapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legatoapp.R;

import java.util.regex.Pattern;

public class forgetPassword extends AppCompatActivity {

    private EditText newPasswordInput, verifyNewPasswordInput, emailInput;
    private TextView passwordErrorMsg, passwordMismatchMsg, emailErrorMsg;
    private Button resetPasswordButton, sendCodeButton;

    boolean isPasswordVisible = false;
    boolean isVerifyPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // UI Elements
        ImageButton backToLogInButton = findViewById(R.id.backToLogInButton);
        ImageView togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        ImageView toggleVerifyPasswordVisibility = findViewById(R.id.toggleVerifyPasswordVisibility);
        newPasswordInput = findViewById(R.id.newPassword);
        verifyNewPasswordInput = findViewById(R.id.verifyNewPassword);
        emailInput = findViewById(R.id.emailInput);
        passwordErrorMsg = findViewById(R.id.passwordErrorText);
        passwordMismatchMsg = findViewById(R.id.passwordMismatchText);
        emailErrorMsg = findViewById(R.id.emailErrorText);
        sendCodeButton = findViewById(R.id.verificationCodePopupButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        //**** At first DISABLE:
        // - Password input fields & visibility buttons: these ones will be enabled until code has been verified
        // - ResetPasswordButton: This one will be enabled until the entire form is validated
        // ****
        newPasswordInput.setAlpha(0.5f); // Set transparency at the start
        newPasswordInput.setEnabled(false); // Disable initially
        togglePasswordVisibility.setEnabled(false); // Disable initially

        verifyNewPasswordInput.setAlpha(0.5f); // Set transparency at the start
        verifyNewPasswordInput.setEnabled(false); // Disable initially
        toggleVerifyPasswordVisibility.setEnabled(false);

        resetPasswordButton.setAlpha(0.5f); // Set transparency at the start
        resetPasswordButton.setEnabled(false); // Disable initially


        //**** Toggle New Password Visibility ****
        togglePasswordVisibility.setOnClickListener(v -> {
            int cursorPosition = newPasswordInput.getSelectionStart();
            Typeface typeface = newPasswordInput.getTypeface();
            if (isPasswordVisible) {
                newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.eye_fill);
            } else {
                newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.eye_slash);
            }
            newPasswordInput.setTypeface(typeface);
            newPasswordInput.setSelection(cursorPosition);
            isPasswordVisible = !isPasswordVisible;
        });

        //**** Toggle Verify New Password Visibility ****
        toggleVerifyPasswordVisibility.setOnClickListener(v -> {
            int cursorPosition = verifyNewPasswordInput.getSelectionStart();
            Typeface typeface = verifyNewPasswordInput.getTypeface();
            if (isVerifyPasswordVisible) {
                verifyNewPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleVerifyPasswordVisibility.setImageResource(R.drawable.eye_fill);
            } else {
                verifyNewPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleVerifyPasswordVisibility.setImageResource(R.drawable.eye_slash);
            }
            verifyNewPasswordInput.setTypeface(typeface);
            verifyNewPasswordInput.setSelection(cursorPosition);
            isVerifyPasswordVisible = !isVerifyPasswordVisible;
        });

        //**** Set an OnClickListener on the BACK TO LOGIN button ****
        backToLogInButton.setOnClickListener(v -> {
            // Create an Intent to start the LoginActivity
            Intent intent = new Intent(forgetPassword.this, userLoginActivity.class);

            // Start the LoginActivity
            startActivity(intent);

            // Finish the current activity to remove it from the back stack
            finish();
        });

        //**** Set an OnClickListener on the RESET PASSWORD button ****
        resetPasswordButton.setOnClickListener(view -> {
            // Get input values
            String email = emailInput.getText().toString().trim();
            String password = newPasswordInput.getText().toString().trim();
            String verifyPassword = verifyNewPasswordInput.getText().toString().trim();

            // Validate inputs
            if (!isValidPassword(password) || !password.equals(verifyPassword)
                    || !isValidEmail(email)) {
                return;
            }

            //RESET PASSWORD LOGIC BACKEND
            resetPasswordLogic();

        });

        //**** Set an OnClickListener on the SEND VERIFICATION CODE button ****
        sendCodeButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();

            // Check if email is empty or invalid
            if (email.isEmpty() || !isValidEmail(email)) {
                emailErrorMsg.setVisibility(View.VISIBLE);
                return; // stop here, don’t open popup
            } else {
                emailErrorMsg.setVisibility(View.GONE);
            }

            // Inflate the popup layout
            View popupView = getLayoutInflater().inflate(R.layout.popup_password_confirmation_code, null);

            // Create a dialog
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(forgetPassword.this);
            builder.setView(popupView);
            android.app.AlertDialog popupDialog = builder.create();

            // Exit button inside popup
            ImageButton exitButton = popupView.findViewById(R.id.popup_exit_button);
            exitButton.setOnClickListener(v -> popupDialog.dismiss());

            // Verify button logic
            Button verifyButton = popupView.findViewById(R.id.popup_verify_button);
            verifyButton.setOnClickListener(v -> {


                //VERIFY PASSWORD LOGIC BACKEND
                verifyButtonLogic();

                //PASSWORD FIELDS ARE ENABLED
                newPasswordInput.setAlpha(1.0f);
                newPasswordInput.setEnabled(true);
                togglePasswordVisibility.setEnabled(true);

                verifyNewPasswordInput.setAlpha(1.0f);
                verifyNewPasswordInput.setEnabled(true);
                toggleVerifyPasswordVisibility.setEnabled(true);

                //SEND CODE BUTTON IS CHANGED AND CHANGED TO GREEN
                sendCodeButton.setEnabled(false);
                sendCodeButton.setText("Email verified");
                sendCodeButton.setTextColor(getResources().getColor(R.color.spotify1));

                //DISABLES EMAIL TEXT BOX
                emailInput.setEnabled(false);
                emailInput.setAlpha(0.5f);

                //CLOSES POPUP
                popupDialog.dismiss();
            });


            // SHOWS POPUP
            popupDialog.show();
        });


        //**** FocusChange to validate email ****
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

        //**** TextWatcher used to validate NEW PASSWORD and verifyNewPassword ****
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Trim the password and verifypassword to remove extra spaces before or after it is entered
                String password = newPasswordInput.getText().toString().trim();
                String verifyPassword = verifyNewPasswordInput.getText().toString().trim();

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

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        //**** Attach the TextWatcher to both password fields ****
        newPasswordInput.addTextChangedListener(passwordWatcher);
        verifyNewPasswordInput.addTextChangedListener(passwordWatcher);

        //**** TextWatcher used to validate form and enable and disable RESET PASSWORD button ****
        TextWatcher formWatcher = new TextWatcher() {
            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = emailInput.getText().toString().trim();
                String password = newPasswordInput.getText().toString().trim();
                String verifyPassword = verifyNewPasswordInput.getText().toString().trim();

                // Check if all fields are valid
                boolean isValidForm =
                        isValidEmail(email) &&
                                isValidPassword(password) &&
                                password.equals(verifyPassword);

                // Enable button only when both conditions are met
                if (isValidForm) {
                    resetPasswordButton.setAlpha(1.0f); // Fully visible
                    resetPasswordButton.setEnabled(true);
                } else {
                    resetPasswordButton.setAlpha(0.5f); // Semi-transparent
                    resetPasswordButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        //**** Attach the TextWatcher to all input fields ****
        emailInput.addTextChangedListener(formWatcher);
        newPasswordInput.addTextChangedListener(formWatcher);
        verifyNewPasswordInput.addTextChangedListener(formWatcher);


    } //END of onCreate

    // *** FUNCTIONS ***

    // Email validation function
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

        // ToDo: Maybe here add the logic to check if the email exists in aws
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

    private void verifyButtonLogic() {
        // TODO: Add AWS backend logic here (VEDI)
    }

    private void resetPasswordLogic() {
        // TODO: Add AWS backend logic here (VEDI)
    }



}