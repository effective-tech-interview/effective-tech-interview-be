package com.sparcs.teamf.oauth2;

import com.sparcs.teamf.jwt.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class RestTemplateOauthRequester implements OauthRequester {

    private final RestTemplate restTemplate;
    private final TokenUtil tokenUtil;

    @Override
    public ProviderProfile requestProfile(Oauth2Client oauth2Client, String code, String requestUri) {
        String accessTokenUri = oauth2Client.getAccessTokenUri(code, requestUri);
        ProviderToken providerToken = restTemplate.postForObject(accessTokenUri, null, oauth2Client.getProviderToken());
        String idToken = providerToken.getIdToken();
        String providerId = tokenUtil.getProviderId(idToken);
        return new ProviderProfile(providerId, oauth2Client.type());
    }
}
