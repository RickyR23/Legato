package com.example.legatoapp.Services.api;

import com.example.legatoapp.Services.api.models.UserModel;
import com.example.legatoapp.Services.api.models.PostModel;
import com.example.legatoapp.Services.api.models.RequestModel;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // GET request example

    // User endpoints
    @POST("api/signup")
    Call<UserModel> signupUser(@Body UserModel user);

    // Post endpoints
    @POST("api/post")
    Call<PostModel> createPost(@Body PostModel post);

    @GET("api/post")
    Call<PostModel> getPost(@Query("song_ID") String songId);

    // You can add more endpoints as needed
    // For example:
    // @PUT("your-endpoint/{id}")
    // Call<RequestModel> updateItem(@Path("id") String id, @Body RequestModel request);

    // @DELETE("your-endpoint/{id}")
    // Call<Void> deleteItem(@Path("id") String id);
} 