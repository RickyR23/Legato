package com.example.legatoapp.Services.api;

import android.util.Log;

import com.example.legatoapp.Services.api.models.UserModel;
import com.example.legatoapp.Services.api.models.PostModel;
import com.example.legatoapp.Services.api.ApiService;
import com.example.legatoapp.TokenManager;
import com.example.legatoapp.AuthSession;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static ApiClient instance;
    private final ApiService apiService;

    private ApiClient() {
        // Get Cognito tokens
        String idToken = AuthSession.getIdToken();
        String refreshToken = AuthSession.getRefreshToken();

        // Create logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        // Create auth interceptor with Cognito token refresh
        AuthInterceptor authInterceptor = new AuthInterceptor(
                idToken,
                refreshToken,
                new TokenManager.TokenRefreshCallback() {
                    @Override
                    public String refreshTokens(String oldToken) {
                        try {
                            return TokenManager.refreshTokensBlocking(refreshToken); // Refresh the token
                        } catch (Exception e) {
                            Log.e("Api Client", "Refresh Token Error", e);
                            return null;
                        }
                    }
                }
        );

        // Create OkHttpClient with interceptors
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amldpl9cw3.execute-api.us-west-1.amazonaws.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create service instance
        this.apiService = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
