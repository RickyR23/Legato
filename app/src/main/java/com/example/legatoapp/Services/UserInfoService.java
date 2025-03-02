package com.example.legatoapp.Services;

import com.example.legatoapp.models.response.SpotifyUserCurrentTrackResponse;
import com.example.legatoapp.models.response.SpotifyUserProfileResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInfoService {

    // Spotify User Data
    @GET("v1/me")
    Observable<SpotifyUserProfileResponse> getUserProfileData(@Header("Authorization")
                                                    String accessToken);

    @GET("v1/me")
    Observable<SpotifyUserCurrentTrackResponse> getUserRecentlyPlayedTracks(@Header("Authorization")
                                                                 String accessToken);
}
