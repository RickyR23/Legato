package com.example.legatoapp;
import java.util.Map;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

//This is the file handling all our Cognito functionalities.

//Creates the class for necessary functions of Cognito, like Signup, login etc
public class CognitoAuth {
    private static final String Client_ID = "5k2eg0nlrv95d5jv5tmiu76dmd";

    //User Signup Function, needs the users username email and password to authenticate user and create userpool user
    public static void signUpUser(String username, String displayName, String email, String password) {
        CognitoIdentityProviderClient client = CognitoService.getCognitoClient();

        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId(Client_ID)
                    .username(username)
                    .password(password)
                    .userAttributes(
                            AttributeType.builder()
                                    .name("email")
                                    .value(email)
                                    .build(),
                            AttributeType.builder()
                                    .name("nickname")
                                    .value(displayName)
                                    .build()
                    )
                    .build();
            SignUpResponse response = client.signUp(signUpRequest);
            System.out.println("User signed up: " + response.userConfirmed());
        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            //TODO: Handle Errors better
        }
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
}
