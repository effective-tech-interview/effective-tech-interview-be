package com.sparcs.teamf.oauth2;

public interface OauthRequester {

    ProviderProfile requestProfile(Oauth2Client oauth2Client, String code, String requestUri);
}
