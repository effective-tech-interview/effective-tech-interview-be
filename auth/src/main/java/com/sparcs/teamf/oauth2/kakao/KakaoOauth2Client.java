package com.sparcs.teamf.oauth2.kakao;

import com.sparcs.teamf.member.ProviderType;
import com.sparcs.teamf.oauth2.Oauth2Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOauth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String scope;
    private final String redirectUri;
    private final String host;
    private final String authorizationPath;
    private final String tokenPath;

    public KakaoOauth2Client(
        @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
        @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
        @Value("${spring.security.oauth2.client.registration.kakao.scope}") String scope,
        @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri,
        @Value("${spring.security.oauth2.client.provider.kakao.authorization-path}") String authorizationPath,
        @Value("${spring.security.oauth2.client.provider.kakao.host}") String host,
        @Value("${spring.security.oauth2.client.provider.kakao.token-path}") String tokenPath
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.redirectUri = redirectUri;
        this.host = host;
        this.authorizationPath = authorizationPath;
        this.tokenPath = tokenPath;
    }

    public Oauth2Client getClient() {
        return Oauth2Client.builder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .redirectUri(redirectUri)
            .scope(scope)
            .host(host)
            .authorizationPath(authorizationPath)
            .tokenPath(tokenPath)
            .type(ProviderType.KAKAO)
            .providerToken(KakaoProviderToken.class)
            .build();
    }
}
