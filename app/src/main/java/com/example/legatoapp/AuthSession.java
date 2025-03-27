package com.example.legatoapp;


//This file allows the Tokens created in CognitoAuth Sign-in to be used globally
public class AuthSession {
    private static String accessToken;
    private static String idToken;
    private static String refreshToken;

    // Set the tokens
    public static void setTokens(String accessToken, String idToken, String refreshToken) {
        AuthSession.accessToken = accessToken;
        AuthSession.idToken = idToken;
        AuthSession.refreshToken = refreshToken;
    }

    // Get the tokens
    public static String getAccessToken() {
        return accessToken;
    }

    public static String getIdToken() {
        return idToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }
}