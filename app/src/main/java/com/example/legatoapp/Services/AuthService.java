package com.example.legatoapp.Services;

import com.example.legatoapp.models.SpotifyAccessTokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {

    @FormUrlEncoded
    @POST("api/token")
    Call<SpotifyAccessTokenResponse> getAccessToken(
            @Field("grant_type") String grantTypem,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri,
            @Header("Authorization") String authorizationHeader
    );

    @FormUrlEncoded
    @POST("api/token")
    Call<SpotifyAccessTokenResponse>  refreshAccessToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Header("Authorization") String authorizationHeader
    );
}
