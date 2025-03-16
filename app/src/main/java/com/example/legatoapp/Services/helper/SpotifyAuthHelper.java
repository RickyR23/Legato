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
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("spotify_prefs", MODE_PRIVATE);
        String refreshToken = prefs.getString("spotify_refresh_token", null);

        if(refreshToken == null){
            Log.w("SpotifyAuthentication", "Refresh token is not stored yet or is null. . .");
            return Observable.error(new Throwable("No refresh token stored. . ."));
        }

        String credentials = context.getString(R.string.CLIENT_ID) + ":" + context.getString(R.string.CLIENT_SECRET);
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return getAuthService(context)
                .refreshAccessToken("refresh_token", refreshToken, authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> {
                    if (response != null) {
                        storeSpotifyTokens(context, response.getAccess_token(), refreshToken, response.getExpires_in());
                        Log.d("SpotifyAuthentication", "Token refreshed successfully");
                    }
                });

    }

    // TODO: instead of having a method to store tokens to sharedPreferences in each individual class, create a utilitiy sharedPreferenceClass that can be extended to store
    //  the data where ever that method is inhereted from
    public static void storeSpotifyTokens(Context context, String accessToken, String refreshToken, int expiresIn){

        if(accessToken == null || refreshToken == null){
            Log.d("SpotifyAuthentication", "AccessToken or refresh token response from API call was null or unsuccessful. . .");
            return;
        }

        long currTime = System.currentTimeMillis();
        long expirationTime = currTime + (expiresIn  * 1000);

        Log.d("SpotifyAuthentication", "******************* \n Access Token: " + accessToken + "\n Token Expiration Time: " + expirationTime + "\n *******************");

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("spotify_access_token", accessToken);
        editor.putString("spotify_refresh_token", refreshToken);
        editor.putLong("spotify_token_expiration", expirationTime);
        editor.putBoolean("isSpotifyTokenReceived", true);
        boolean success = editor.commit();

        if (success) {
            Log.d("SpotifyAuthentication", "Token Saved to SharedPreferences");
        } else {
            Log.e("SpotifyAuthentication", "Error saving token to SharedPreferences");
        }
    }

     public static boolean isTokenExpired(Context context){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("LegatoPrefs", MODE_PRIVATE);
        long expirationTime = preferences.getLong("spotify_token_expiration", 0);
        long currTime = System.currentTimeMillis();

        if(currTime >= expirationTime){
            return true;
            // token would be expired in this case
        }
        else{
            return false;
            // and token would be still valid in this case
        }
     }

}
