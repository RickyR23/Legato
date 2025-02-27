package com.example.legatoapp.Services.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.legatoapp.Services.AuthService;
import com.example.legatoapp.models.SpotifyAccessTokenResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyAuthHelper {

    private static final String CLIENT_ID = ""; //Always Delete these values prior to pushing to online repo
    private static final String CLIENT_SECRET = ""; //Always Delete these values prior to pushing to online repo
    private static final String REDIRECT_URI = "com.example.legatoapp://callback";
    private static final String SCOPES = "user-read-playback-state user-read-currently-playing";
    private static final String BASE_URL = "https://accounts.spotify.com/";

    private static AuthService authService;



    private static AuthService getAuthService(){
        if(authService == null){
            Retrofit retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Add RxJava support
                    .build();
            authService = retrofitInstance.create(AuthService.class);
        }
        return authService;
    }

    public static Observable<SpotifyAccessTokenResponse> fetchAccessToken(String authToken){
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return getAuthService()
                .getAccessToken("authorization_code", authToken, REDIRECT_URI, authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SpotifyAccessTokenResponse> refreshAccessToken(Context context){
        SharedPreferences prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("refresh_token", null);

        if(refreshToken == null){
            Log.w("SpotifyAuthentication", "Refresh token is not stored yet or is null. . .");
            return Observable.error(new Throwable("No refresh token stored. . ."));
        }

        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return getAuthService()
                .refreshAccessToken("refresh_token", refreshToken, authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // TODO: instead of having a method to store tokens to sharedPreferences in each individual class, create a utilitiy sharedPreferenceClass that can be extended to store
    //  the data where ever that method is inhereted from
    private void storeSpotifyTokens(String accessToken, String refreshToken){
        getSharedPreferences("LegatoPrefs", MODE_PRIVATE)
                .edit()
                .putString("spotify_access_token", accessToken)
                .putString("spotify_refresh_token", refreshToken)
                .apply();

        SharedPreferences sharedPreferences = getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSpotifyTokenReceived", true);
        editor.apply();
    }

}
