package com.example.legatoapp.Services.api;

import android.util.Log;
import com.example.legatoapp.TokenManager;
import com.example.legatoapp.AuthSession;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private String idToken;
    private String refreshToken;
    private TokenManager.TokenRefreshCallback tokenRefreshCallback;

    // Constructor to initialize with tokens and callback
    public AuthInterceptor(String idToken, String refreshToken, TokenManager.TokenRefreshCallback tokenRefreshCallback) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.tokenRefreshCallback = tokenRefreshCallback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + idToken)
                .method(original.method(), original.body())
                .build();

        Response response = chain.proceed(request);

        if (response.code() == 401 && refreshToken != null) {
            response.close();  // Close the failed response

            final Response[] finalResponse = {response};  // Wrap in an array to make it effectively final

            TokenManager.refreshSession(refreshToken, new TokenManager.Callback() {
                @Override
                public void onSuccess() {
                    String newIdToken = AuthSession.getIdToken(); // Update with the new token
                    if (newIdToken != null) {
                        // Retry the original request with the new token
                        Request newRequest = original.newBuilder()
                                .header("Authorization", "Bearer " + newIdToken)
                                .method(original.method(), original.body())
                                .build();

                        try {
                            // Proceed with the retried request
                            finalResponse[0] = chain.proceed(newRequest);  // Update the response
                        } catch (IOException e) {
                            Log.e("AuthInterceptor", "Retrying request failed: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("AuthInterceptor", "Token refresh failed: " + errorMessage);
                }
            });

            // Return the response after retry
            return finalResponse[0];  // Return the updated response after retrying
        }

        return response;
    }
}
