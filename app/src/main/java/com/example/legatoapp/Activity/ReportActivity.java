package com.example.legatoapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.legatoapp.R;

public class ReportActivity extends AppCompatActivity {
    // UI elements
    private EditText reportMessageHeader;
    private EditText reportMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);

        // Back to Settings Button
        ImageButton backToSettings = findViewById(R.id.backToSettings);
        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks for unsaved changes before returning to settings
                checkForUnsavedChanges();
            }
        });

        reportMessageHeader = findViewById(R.id.reportMessageHeader);
        reportMessage = findViewById(R.id.reportMessage);
        Button submitButton = findViewById(R.id.submitButton);

        // Handle submit button action
        submitButton.setOnClickListener(view -> {
            // Obtain message entered
            String messageHeader = reportMessageHeader.getText().toString().trim();
            String messageBody = reportMessage.getText().toString().trim();

            // Check that both message title and body are not left empty
            if (messageHeader.isEmpty() || messageBody.isEmpty()) {
                Toast.makeText(ReportActivity.this, "Please make sure all fields are filled", Toast.LENGTH_SHORT).show();
            } else {
                // Show success message
                Toast.makeText(ReportActivity.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();

                // ToDo: Here is where we will call a method to send report data to server
                // Example - sendReportToServer(messageHeader, messageBody);

                // After submitting report, end activity and return to Settings
                finish();
            }
        });
    } //END of onCreate

    /*-------------METHODS-------------*/
    // Checks if there are unsaved changes before returning to settings, if so display confirmation message
    private void checkForUnsavedChanges() {
        String messageHeader = reportMessageHeader.getText().toString().trim();
        String messageBody = reportMessage.getText().toString().trim();

        // If there are unsaved changes, show a confirmation dialog
        if (!messageHeader.isEmpty() || !messageBody.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("Are you sure you want to leave without submitting the report?")
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

    // This is a placeholder in the meantime
    private void sendReportToServer(String header, String body) {
        // ToDo: Here is where we will implement the logic to send report data to server
    }

} //END of class
