package com.sparcs.teamf.oauth2;

import com.sparcs.teamf.member.ProviderType;
import lombok.Builder;
import org.springframework.web.util.UriComponentsBuilder;

@Builder
public record Oauth2Client(String clientId,
                           String clientSecret,
                           String redirectUri,
                           String scope,
                           String host,
                           String authorizationPath,
                           String tokenPath,
                           ProviderType type,
                           Class<? extends ProviderToken> providerToken) {

    public String getProviderRedirectUri(String redirectUri) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(host)
            .path(authorizationPath)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_type", "code")
            .queryParam("scope", scope)
            .build(true)
            .toString();
    }

    public String getAccessTokenUri(String code, String redirectUri) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(host)
            .path(tokenPath)
            .toUriString() +
            "?grant_type=authorization_code&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri="
            + redirectUri + "&code=" + code;
    }

    public Class<? extends ProviderToken> getProviderToken() {
        return providerToken;
    }
}
