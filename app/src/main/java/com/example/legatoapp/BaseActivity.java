package com.example.legatoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;



public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
    }
        }
    }

    FirebaseMessaging{getInstance().getToken()
        .addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.w("FCM", "Fetching FCM token failed", task.getException());
                return;
            
            }
            String token = task.getResult();
            Log.d("FCM Token", token);
        });
    }
    }
