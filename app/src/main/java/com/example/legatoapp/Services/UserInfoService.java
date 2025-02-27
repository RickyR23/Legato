package com.example.legatoapp.Services;

import com.example.legatoapp.models.SpotifyUserProfileResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInfoService {


    @GET("v1/me")
    Observable<SpotifyUserProfileResponse> getUserProfileData(
            @Header("Authorization") String accessToken
    );
}
