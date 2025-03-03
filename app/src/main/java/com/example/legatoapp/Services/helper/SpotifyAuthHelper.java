package com.example.legatoapp.Services.helper;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;


import com.example.legatoapp.R;
import com.example.legatoapp.Services.AuthService;
import com.example.legatoapp.models.response.SpotifyAccessTokenResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyAuthHelper {

    private static AuthService authService;

    //TODO: Update Documentation

    /**
     * This obtains the authentication URI for a users Spotify Account
     * @return AuthService object that contains the spotify account link
     */
    private static AuthService getAuthService(Context context){
        if(authService == null){
            Retrofit retrofitInstance = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Add RxJava support
                    .build();
            authService = retrofitInstance.create(AuthService.class);
        }
        return authService;
    }

    /**
     * This helps fetch an authorized spotify access token using our projects CLIENT ID and CLIENT secret
     * @return . . .
     */
    public static Observable<SpotifyAccessTokenResponse> fetchAccessToken(Context context, String authToken){
        String credentials = context.getString(R.string.CLIENT_ID) + ":" + context.getString(R.string.CLIENT_SECRET);
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return getAuthService(context)
                .getAccessToken("authorization_code", authToken, context.getString(R.string.REDIRECT_URI), authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * In the instance
     * @param context
     * @return . . .
     */
    public static Observable<SpotifyAccessTokenResponse> refreshAccessToken(Context context){
        SharedPreferences prefs = context.getSharedPreferences("spotify_prefs", MODE_PRIVATE);
        String refreshToken = prefs.getString("refresh_token", null);

        if(refreshToken == null){
            Log.w("SpotifyAuthentication", "Refresh token is not stored yet or is null. . .");
            return Observable.error(new Throwable("No refresh token stored. . ."));
        }

        String credentials = context.getString(R.string.CLIENT_ID) + ":" + context.getString(R.string.CLIENT_SECRET);
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return getAuthService(context)
                .refreshAccessToken("refresh_token", refreshToken, authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    // TODO: instead of having a method to store tokens to sharedPreferences in each individual class, create a utilitiy sharedPreferenceClass that can be extended to store
    //  the data where ever that method is inhereted from
    public static void storeSpotifyTokens(Context context, String accessToken, String refreshToken){

        SharedPreferences preferences = context.getSharedPreferences("LegatoPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putString("spotify_access_token", accessToken);
        editorPref.putString("spotify_refresh_token", refreshToken);
        editorPref.apply();

        SharedPreferences sharedPreferences = context.getSharedPreferences("LegatoPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSpotifyTokenReceived", true);
        editor.apply();
    }

}
