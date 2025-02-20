package com.example.legatoapp.models;

public class SpotifyAccessTokenResponse {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;

    public String getAccess_token(){
        return access_token;
    }

    public String getRefresh_token(){
        return refresh_token;
    }
}
