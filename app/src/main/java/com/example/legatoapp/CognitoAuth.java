package com.example.legatoapp;
import android.content.Context;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

//This is the file handling all our Cognito functionalities.

//Creates the class for necessary functions of Cognito, like Signup, login etc

public class CognitoAuth {
    private static String Client_ID;
    private static String Userpool_ID;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void initialize(Context context) {
        Client_ID = context.getString(R.string.COGNITO_CLIENT_ID);
        Userpool_ID = context.getString(R.string.COGNITO_USERPOOL_ID);
    }
    public static void signUpUser(String username, String displayName, String email, String password) {
        executorService.execute(() -> {
            CognitoIdentityProviderClient client = CognitoService.getCognitoClient();
            try {
                SignUpRequest signUpRequest = SignUpRequest.builder()
                        .clientId(Client_ID)
                        .username(username)
                        .password(password)
                        .userAttributes(
                                AttributeType.builder().name("email").value(email).build(),
                                AttributeType.builder().name("preferred_username").value(displayName).build()
                        )
                        .build();

                SignUpResponse response = client.signUp(signUpRequest);
                System.out.println("User signed up: " + response.userConfirmed());
            } catch (CognitoIdentityProviderException e) {
                System.err.println("Signup Error: " + e.awsErrorDetails().errorMessage());
            }
        });
    }
    //User Login Functionality and gaining Tokens
    public static void signInUser(String username, String password) {
        CognitoIdentityProviderClient client = CognitoService.getCognitoClient();

        try {
            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .clientId(Client_ID)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .authParameters(Map.of(
                            "USERNAME", username,
                            "PASSWORD", password
                    ))
                    .build();

            InitiateAuthResponse response = client.initiateAuth(authRequest);

            AuthSession.setTokens(
                    response.authenticationResult().accessToken(),
                    response.authenticationResult().idToken(),
                    response.authenticationResult().refreshToken()
            );

        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            //TODO: Handle Errors Better
        }
    }

    //For confirming if the user is existent or not
    public static void confirmUser(String username, String confirmationCode) {
        CognitoIdentityProviderClient client = CognitoService.getCognitoClient();

        try {
            ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(Client_ID)
                    .confirmationCode(confirmationCode)
                    .username(username)
                    .build();


            client.confirmSignUp(signUpRequest);
            System.out.println(username + " was confirmed");

        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            //TODO: Handle Errors better
        }
    }

    //Forgot password functionality
    public static void forgotPassword(String username) {
        CognitoIdentityProviderClient client = CognitoService.getCognitoClient();

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

    public static boolean isUserConfirmed(String username) {
        CognitoIdentityProviderClient client = CognitoService.getCognitoClient();

        try {
            AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder()
                    .userPoolId(Userpool_ID)
                    .username(username)
                    .build();

            AdminGetUserResponse getUserResponse = client.adminGetUser(getUserRequest);

            for (AttributeType attribute : getUserResponse.userAttributes()) {
                if (attribute.name().equals("cognito:user_status")) {
                    return attribute.value().equals("CONFIRMED");
                }
            }
        } catch (CognitoIdentityProviderException e) {
            System.err.println("Error checking user status: " + e.awsErrorDetails().errorMessage());
        }

        return false;
    }
}
