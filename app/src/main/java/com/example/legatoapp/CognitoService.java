
package com.example.legatoapp;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

//Initializes an instance of CognitoIdentity Provider so we can connect to our Cognito Instance on AWS
public class CognitoService {
    private static final CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
            .httpClient(UrlConnectionHttpClient.create())
            .region(Region.US_WEST_1)
            .build();

    public static CognitoIdentityProviderClient getCognitoClient() {
        return cognitoClient;
    }
}
