package com.example.legatoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.legatoapp.Services.AuthService;
import com.example.legatoapp.models.SpotifyAccessTokenResponse;

import java.net.URI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signUp extends AppCompatActivity {

    private static final String CLIENT_ID = ""; //Always Delete these values prior to pushing to online repo
    private static final String CLIENT_SECRET = ""; //Always Delete these values prior to pushing to online repo
    private static final String REDIRECT_URI = ""; //Always Delete these values prior to pushing to online repo
    private static final String SCOPES = "user-read-playback-state user-read-currently-playing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ImageButton backToLogInButton = findViewById(R.id.backToLogInButton);
        backToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, userLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button connectSpotifyButton = findViewById(R.id.buttonConnectSpotify);
        connectSpotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSpotifyAuthSession();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri receivedUri = getIntent().getData();

        if(receivedUri != null && receivedUri.toString().startsWith(REDIRECT_URI)){
            String authCode = receivedUri.getQueryParameter("code");
            if(authCode != null){
                exchangeAuthorizationForToken(authCode);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void launchSpotifyAuthSession(){
        Uri authenticationURI = new Uri.Builder()
                .scheme("https")
                .authority("accounts.spotify.com")
                .appendPath("authorize")
                .appendQueryParameter("client_id",CLIENT_ID)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("scope", SCOPES)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, authenticationURI);
        startActivity(intent);
    }

    private void exchangeAuthorizationForToken(String code){
        Retrofit retrofitInstance = new Retrofit.Builder()
                .baseUrl("https://accounts.spotify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService authenticationService = retrofitInstance.create(AuthService.class);

        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Call<SpotifyAccessTokenResponse> call = authenticationService.getAccessToken(
                "authorization_code", code, REDIRECT_URI, authHeader
        );

        call.enqueue(new Callback<SpotifyAccessTokenResponse>() {
            @Override
            public void onResponse(Call<SpotifyAccessTokenResponse> call, Response<SpotifyAccessTokenResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    String retrievedAccessToken = response.body().getAccess_token();
                    String retrievedRefreshToken = response.body().getRefresh_token();
                    storeSpotifyTokens(retrievedAccessToken, retrievedRefreshToken);

                    Log.d("SpotifyAuthService", "Spotify has received token response: \n Access Token: " + retrievedAccessToken + "\n refreshToken: " + retrievedRefreshToken);
                }
            }

            @Override
            public void onFailure(Call<SpotifyAccessTokenResponse> call, Throwable t) {
                Log.d("SpotifyAuthService", "Spotify AuthService failed to obtain tokens with retrieved error: \n" + t.getMessage());
            }
        });

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