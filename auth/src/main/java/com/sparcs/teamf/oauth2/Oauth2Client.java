package com.sparcs.teamf.oauth2;

import lombok.Builder;

@Builder
public record Oauth2Client(String clientId,
                           String clientSecret,
                           String redirectUri,
                           String scope,
                           String authorizeUri) {

}
