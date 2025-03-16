package com.example.legatoapp.models.response;

import com.google.gson.annotations.SerializedName;

public class SpotifyAccessTokenResponse {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("token_type")
    private String token_type;

    @SerializedName("expires_in")
    private int expires_in;

    @SerializedName("refresh_token")
    private String refresh_token;

    public String getAccess_token(){
        return access_token;
    }

    public String getRefresh_token(){
        return refresh_token;
    }

    public int getExpires_in(){
        return expires_in;
    }

    public String getToken_type(){
        return token_type;
    }
}
