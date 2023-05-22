package com.sparcs.teamf.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOauth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String scope;
    private final String redirectUri;

    public KakaoOauth2Client(
        @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
        @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
        @Value("${spring.security.oauth2.client.registration.kakao.scope}") String scope,
        @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.redirectUri = redirectUri;
    }

    public Oauth2Client getClient() {
        return Oauth2Client.builder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .redirectUri(redirectUri)
            .scope(scope)
            .build();
    }
}
