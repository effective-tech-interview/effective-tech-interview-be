package com.sparcs.teamf.oauth2.kakao;

import com.sparcs.teamf.oauth2.ProviderToken;

public record KakaoTokenResponse(String access_token,
                                 String token_type,
                                 String refresh_token,
                                 int expires_in,
                                 String scope,
                                 String id_token,
                                 int refresh_token_expires_in) implements ProviderToken {

    @Override
    public String getAccessToken() {
        return access_token;
    }

    @Override
    public String getRefreshToken() {
        return refresh_token;
    }

    @Override
    public String getIdToken() {
        return id_token;
    }
}
