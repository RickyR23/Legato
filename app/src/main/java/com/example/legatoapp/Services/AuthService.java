package com.example.legatoapp.Services;

import com.example.legatoapp.models.response.SpotifyAccessTokenResponse;
import com.example.legatoapp.models.response.SpotifyUserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {

    // Spotify Auth Tokens --------------
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


    // Spotify User Data
    @GET("v1/me")
    Call<SpotifyUserProfileResponse> getUserProfile(@Header("Authorization")
                                                    String accessToken);

    @GET("v1/me")
    Call<SpotifyUserProfileResponse> getUserRecentlyPlayedTracks(@Header("Authorization")
                                                                 String accessToken);

}
