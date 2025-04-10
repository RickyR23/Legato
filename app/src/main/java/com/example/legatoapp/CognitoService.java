package com.example.legatoapp;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderAsyncClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;


public class CognitoService {
    private static final CognitoIdentityProviderAsyncClient cognitoClient = CognitoIdentityProviderAsyncClient.builder()
            .httpClient(NettyNioAsyncHttpClient.builder().build()) // Use Netty for async HTTP calls
            .region(Region.US_WEST_1) // Set the region
            .build();

    public static CognitoIdentityProviderAsyncClient getCognitoClient() {
        return cognitoClient;
    }
}