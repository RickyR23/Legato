package com.example.legatoapp.Services.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.legatoapp.R;
import com.example.legatoapp.Services.UserInfoService;
import com.example.legatoapp.models.response.SpotifyUserCurrentTrackResponse;
import com.example.legatoapp.models.response.SpotifyUserProfileResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyProfileDataHelper {
    private static UserInfoService userInfoService;

    private static UserInfoService getSpotifyAuthentication(Context context){
        if(userInfoService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            userInfoService = retrofit.create(UserInfoService.class);
        }
        return userInfoService;
    }

    /**
     * Fetches user profile data from UserInfoService calls
     */
    public static Observable<SpotifyUserProfileResponse> fetchSpotifyUserProfile(Context context){
        SharedPreferences preferences = context.getSharedPreferences("LegatoPrefs", Context.MODE_PRIVATE);
        String accessToken = preferences.getString("spotify_access_token", null);

        if(accessToken == null){
            returnErrorMessage();
        }

        String authHeader = "Bearer " + accessToken;

        return getSpotifyAuthentication(context)
                .getUserProfileData(authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SpotifyUserCurrentTrackResponse> fetchSpotifyUserLastTrackPlayed(Context context){
        SharedPreferences preferences = context.getSharedPreferences("LegatoPrefs", Context.MODE_PRIVATE);
        String accessToken = preferences.getString("spotify_access_token", null);

        if(accessToken == null){
            returnErrorMessage();
        }

        String authHeader = "Bearer " + accessToken;

        return getSpotifyAuthentication(context)
                .getUserRecentlyPlayedTracks(authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Observable<Error> returnErrorMessage(){
        Log.e("SpotifyProfileDataHelper", "No access token has been saved . . .");
        return  Observable.error(new Throwable("Missing spotify access token"));
    }
}
