package com.example.legatoapp.Services.api;


import android.util.Log;

import com.example.legatoapp.Services.api.models.PostModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostService {
    private final ApiService apiService;

    public PostService() {
        // Get the API client instance
        this.apiService = ApiClient.getInstance().getApiService();
    }


    public void createPost(String user_Id, String spotify_Id, String caption) {
        // Create user model
        PostModel newPost = new PostModel(
                user_Id,
                spotify_Id,
                caption
        );

        // Make API call
        apiService.createPost(newPost).enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.isSuccessful()) {
                    Log.d("PostService", "Post created: " + response.body());
                    // String userId = response.body().getUserId();
                } else {
                    Log.e("PostService", "Create Post failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.e("PostService", "Network error", t);
            }
        });
    }
}


