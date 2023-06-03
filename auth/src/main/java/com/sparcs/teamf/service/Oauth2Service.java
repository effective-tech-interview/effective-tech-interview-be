package com.sparcs.teamf.service;

import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.jwt.TokenProvider;
import com.sparcs.teamf.oauth2.EffectiveProfile;
import com.sparcs.teamf.oauth2.Oauth2Client;
import com.sparcs.teamf.oauth2.OauthRequester;
import com.sparcs.teamf.oauth2.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Oauth2Service {

    private final SignupService signupService;
    private final TokenProvider tokenProvider;
    private final OauthRequester oauthRequester;


    public String getRedirectUri(Oauth2Client oauth2Client, String requestUri) {
        return oauth2Client.getProviderRedirectUri(requestUri);
    }

    public TokenResponse login(Oauth2Client client, String code, String requestUri) {
        ProviderProfile providerProfile = oauthRequester.requestProfile(client, code, requestUri);
        //todo logout 을 위한 토큰 관리 기능 추가
        EffectiveProfile effectiveProfile = signupService.registerAndLogin(providerProfile.providerId(),
            providerProfile.provider().name());
        return tokenProvider.createToken(effectiveProfile.memberId());
    }
}
