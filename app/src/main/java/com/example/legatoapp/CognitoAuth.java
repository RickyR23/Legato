package com.example.legatoapp;
import android.content.Context;
import android.util.Log;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//This is the file handling all our Cognito functionalities.

//Creates the class for necessary functions of Cognito, like Signup, login etc

public class CognitoAuth {
    public static String Client_ID;
    public static String Userpool_ID;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface Callback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public static void initialize(Context context) {
        Client_ID = context.getString(R.string.COGNITO_CLIENT_ID);
        Userpool_ID = context.getString(R.string.COGNITO_USERPOOL_ID);
    }
    public static void signUpUser(String username, String displayName, String email, String password, Callback callback) {
        // Getting the async Cognito client
        CognitoIdentityProviderAsyncClient client = CognitoService.getCognitoClient();

        // Build the sign-up request
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(Client_ID)
                .username(username)
                .password(password)
                .userAttributes(
                        AttributeType.builder().name("email").value(email).build(),
                        AttributeType.builder().name("preferred_username").value(displayName).build()
                )
                .build();

        // Asynchronously call the Cognito signUp method
        client.signUp(signUpRequest).whenComplete((response, error) -> {
            if (error != null) {
                // Handle error in sign-up process
                if (error instanceof CognitoIdentityProviderException) {
                    CognitoIdentityProviderException e = (CognitoIdentityProviderException) error;
                    System.err.println("Signup Error: " + e.awsErrorDetails().errorMessage());
                    callback.onFailure(e.awsErrorDetails().errorMessage());
                } else {
                    System.err.println("Unexpected error: " + error.getMessage());
                    callback.onFailure("Unexpected error: " + error.getMessage());
                }
                return;
            }

            // Success scenario
            System.out.println("User signed up: " + response.userConfirmed());
            callback.onSuccess();
        });
    }
    //User Login Functionality and gaining Tokens

    public static void signInUser(String username, String password, Callback callback) {
        CognitoIdentityProviderAsyncClient client = CognitoService.getCognitoClient();
        InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                .clientId(Client_ID)
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .authParameters(Map.of(
                        "USERNAME", username,
                        "PASSWORD", password
                ))
                .build();

        client.initiateAuth(authRequest).whenComplete((response, error) -> {
            if (error != null) {
                if (error instanceof CognitoIdentityProviderException) {
                    CognitoIdentityProviderException e = (CognitoIdentityProviderException) error;
                    Log.e("SignInError", "Login failed: " + e.awsErrorDetails().errorMessage());
                    callback.onFailure(e.awsErrorDetails().errorMessage());
                } else {
                    Log.e("SignInError", "Unexpected error: " + error.getMessage());
                    callback.onFailure("Unexpected error: " + error.getMessage());
                }
                return;
            }

            // Save tokens
            AuthSession.setTokens(
                    response.authenticationResult().accessToken(),
                    response.authenticationResult().idToken(),
                    response.authenticationResult().refreshToken()
            );

            callback.onSuccess();
        });
    }

    //For confirming if the user is existent or not
    public static void confirmUser(String username, String confirmationCode, Callback callback) {
        CognitoIdentityProviderAsyncClient client = CognitoService.getCognitoClient();

        ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                .clientId(Client_ID)
                .confirmationCode(confirmationCode)
                .username(username)
                .build();

        client.confirmSignUp(signUpRequest).whenComplete((response, error) -> {
            if (error != null) {
                if (error instanceof CognitoIdentityProviderException) {
                    CognitoIdentityProviderException e = (CognitoIdentityProviderException) error;
                    System.err.println("Confirmation failed: " + e.awsErrorDetails().errorMessage());
                    callback.onFailure(e.awsErrorDetails().errorMessage());
                } else {
                    System.err.println("Unexpected error: " + error.getMessage());
                    callback.onFailure("Unexpected error: " + error.getMessage());
                }
                return;
            }

            System.out.println(username + " was confirmed successfully");
            callback.onSuccess();
        });
    }


    //Forgot password functionality
    public static void forgotPassword(String username) {
        CognitoIdentityProviderAsyncClient client = CognitoService.getCognitoClient();

        try {
            ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                    .clientId(Client_ID)
                    .username(username)
                    .build();

            client.forgotPassword(request);
            System.out.println("Password reset code sent to email.");
        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            //TODO: Handle errors better
        }

    }

}
