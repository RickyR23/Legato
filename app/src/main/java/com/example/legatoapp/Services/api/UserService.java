package com.example.legatoapp.Services.api;


import android.util.Log;

import com.example.legatoapp.Services.api.models.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserService {
    private final ApiService apiService;

    public UserService() {
        // Get the API client instance
        this.apiService = ApiClient.getInstance().getApiService();
    }


    public void signupUser(String email, String username, String spotify_ID) {
        // Create user model
        UserModel newUser = new UserModel(
                email,
                username,
                spotify_ID // Default spotify_ID
        );

        // Make API call
        apiService.signupUser(newUser).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Log.d("UserService", "User created: " + response.body());
                } else {
                    Log.e("UserService", "Signup failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("UserService", "Network error", t);
            }
        });
    }
}


