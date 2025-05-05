package com.example.legatoapp.Services.api;

import android.util.Log;

import com.example.legatoapp.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private String idToken;
    private String refreshToken;
    private final TokenManager.TokenRefreshCallback tokenRefreshCallback;

    public AuthInterceptor(String idToken, String refreshToken, TokenManager.TokenRefreshCallback tokenRefreshCallback) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.tokenRefreshCallback = tokenRefreshCallback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Attach the initial token
        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + idToken)
                .method(original.method(), original.body())
                .build();

        Log.d("AuthInterceptor", "Sending request to: " + request.url());

        Response response = chain.proceed(request);

        if (response.code() == 401 && refreshToken != null) {
            Log.w("AuthInterceptor", "401 Unauthorized - attempting to refresh token");

            response.close(); // Always close the first response

            try {
                // Synchronously refresh token
                String newIdToken = TokenManager.refreshTokensBlocking(refreshToken);
                if (newIdToken != null) {
                    Log.d("AuthInterceptor", "Token refreshed successfully");

                    this.idToken = newIdToken;

                    // Notify callback (if used to update ApiClient or storage)
                    if (tokenRefreshCallback != null) {
                        tokenRefreshCallback.refreshTokens(newIdToken);
                    }

                    // Retry request with new token
                    Request newRequest = original.newBuilder()
                            .header("Authorization", "Bearer " + newIdToken)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(newRequest);
                } else {
                    Log.e("AuthInterceptor", "Failed to refresh token: null returned");
                }
            } catch (Exception e) {
                Log.e("AuthInterceptor", "Token refresh error", e);
            }
        }

        return response;
    }
}