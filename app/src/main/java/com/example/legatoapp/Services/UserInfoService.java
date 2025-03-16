package com.example.legatoapp.Services;

import com.example.legatoapp.models.response.SpotifyUserRecentlyPlayedTracksResponse;
import com.example.legatoapp.models.response.SpotifyUserProfileResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInfoService {

    // Spotify User Data
    @GET("v1/me")
    Observable<SpotifyUserProfileResponse> getUserProfileData(@Header("Authorization")
                                                    String authorizationHeader);

    //Spotify current track playing
    @GET("v1/me/player/currently-playing")
    Observable<SpotifyUserRecentlyPlayedTracksResponse> getUserCurrentPlayingTrack(@Header("Authorization")
                                                                 String authorizationHeader);

    @GET("v1/me/player/recently-played?limit=1")
    Observable<SpotifyUserRecentlyPlayedTracksResponse> getUserRecentlyPlayedTracks(@Header("Authorization")
                                                                                   String authorizationHeader);
}
