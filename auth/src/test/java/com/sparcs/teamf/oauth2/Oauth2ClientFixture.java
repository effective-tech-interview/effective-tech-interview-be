package com.sparcs.teamf.oauth2;

import com.sparcs.teamf.member.ProviderType;

public class Oauth2ClientFixture {

    public static final Oauth2Client oauth2Client = new Oauth2Client(
        "clientId",
        "clientSecret",
        "redirectUri",
        "scope",
        "host",
        "authorizationPath",
        "tokenPath",
        ProviderType.KAKAO,
        ProviderToken.class);
}
