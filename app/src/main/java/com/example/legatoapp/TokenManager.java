package com.example.legatoapp;

import android.util.Log;

import com.example.legatoapp.AuthSession;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

public class TokenManager {

    public interface Callback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public interface TokenRefreshCallback {
        String refreshTokens(String refreshToken) throws Exception;
    }
    public static void refreshSession(String refreshToken, Callback callback) {
        CognitoIdentityProviderAsyncClient client = CognitoService.getCognitoClient();

        InitiateAuthRequest refreshRequest = InitiateAuthRequest.builder()
                .clientId(AuthSession.CLIENT_ID) // Or fetch dynamically
                .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .authParameters(Map.of("REFRESH_TOKEN", refreshToken))
                .build();

        client.initiateAuth(refreshRequest).whenComplete((response, error) -> {
            if (error != null) {
                Log.e("TokenManager", "Refresh failed: " + error.getMessage());
                callback.onFailure("Token refresh failed");
                return;
            }

            AuthSession.setTokens(
                    response.authenticationResult().accessToken(),
                    response.authenticationResult().idToken(),
                    refreshToken // Same refresh token is still valid
            );

            callback.onSuccess();
        });
    }
    public static String refreshTokensBlocking(String refreshToken) throws Exception {
        CognitoIdentityProviderClient client = CognitoIdentityProviderClient.create();

        InitiateAuthRequest refreshRequest = InitiateAuthRequest.builder()
                .clientId(AuthSession.CLIENT_ID)
                .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .authParameters(Map.of("REFRESH_TOKEN", refreshToken))
                .build();

        try {
            InitiateAuthResponse response = client.initiateAuth(refreshRequest);

            String newIdToken = response.authenticationResult().idToken();
            String newAccessToken = response.authenticationResult().accessToken();

            // Save updated tokens
            AuthSession.setTokens(newAccessToken, newIdToken, refreshToken);

            return newIdToken;  // return the new ID token
        } catch (CognitoIdentityProviderException e) {
            Log.e("TokenManager", "Cognito error during token refresh: " + e.awsErrorDetails().errorMessage());
            throw new Exception("Cognito token refresh error: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            Log.e("TokenManager", "Unexpected error: " + e.getMessage());
            throw new Exception("Unexpected error: " + e.getMessage());
        }

    }

}
