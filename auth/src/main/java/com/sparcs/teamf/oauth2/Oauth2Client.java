package com.sparcs.teamf.oauth2;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Oauth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String scope;
}
