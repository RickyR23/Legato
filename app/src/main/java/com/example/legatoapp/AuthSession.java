package com.example.legatoapp;

public class AuthSession {
    private static String accessToken;
    private static String idToken;
    private static String refreshToken;

    public static final String CLIENT_ID = CognitoAuth.Client_ID;

    public static void setTokens(String access, String id, String refresh) {
        accessToken = access;
        idToken = id;
        refreshToken = refresh;
    }

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