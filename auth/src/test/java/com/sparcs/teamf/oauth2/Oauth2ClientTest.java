package com.sparcs.teamf.oauth2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Oauth2Client 의 url 을 생성하는 기능을 테스트한다.")
class Oauth2ClientTest {


    @Test
    void redirectUrl_을_잘_가져올_수_있다() {
        Oauth2Client oauth2Client = Oauth2ClientFixture.oauth2Client;

        String result = oauth2Client.getProviderRedirectUri("requestUri");
        assertThat(result).isEqualTo(
            "https://host/authorizationPath?client_id=clientId&redirect_uri=requestUri&response_type=code&scope=scope");
    }

    @Test
    void accessTokenUri_을_잘_가져올_수_있다() {
        Oauth2Client oauth2Client = Oauth2ClientFixture.oauth2Client;

        String result = oauth2Client.getAccessTokenUri("code", "requestUri");
        assertThat(result).isEqualTo(
            "https://host/tokenPath?grant_type=authorization_code&client_id=clientId&client_secret=clientSecret&redirect_uri=requestUri&code=code");
    }

    @Test
    void providerToken_을_잘_가져올_수_있다() {
        Oauth2Client oauth2Client = Oauth2ClientFixture.oauth2Client;

        Class<? extends ProviderToken> result = oauth2Client.getProviderToken();
        assertThat(result).isEqualTo(ProviderToken.class);
    }
}
