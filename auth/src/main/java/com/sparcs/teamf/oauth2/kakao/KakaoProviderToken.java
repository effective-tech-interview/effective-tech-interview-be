package com.sparcs.teamf.oauth2.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sparcs.teamf.oauth2.ProviderToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoProviderToken implements ProviderToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresin;
    private String scope;
    private String refreshTokenExpiresIn;
    private String idToken;


    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String getIdToken() {
        return idToken;
    }
}
