package com.sparcs.teamf.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.jwt.TokenProvider;
import com.sparcs.teamf.member.ProviderType;
import com.sparcs.teamf.oauth2.EffectiveProfile;
import com.sparcs.teamf.oauth2.Oauth2Client;
import com.sparcs.teamf.oauth2.Oauth2ClientFixture;
import com.sparcs.teamf.oauth2.OauthRequester;
import com.sparcs.teamf.oauth2.ProviderProfile;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class Oauth2ServiceTest {

    @InjectMocks
    private Oauth2Service oauth2Service;

    @Mock
    private OauthRequester oauthRequester;

    @Mock
    private SignupService signupService;

    @Mock
    private TokenProvider tokenProvider;

    @Test
    void 정상적으로_리다이렉트_URI_를_생성한다() {
        Oauth2Client oauth2Client = Oauth2ClientFixture.oauth2Client;

        String result = oauth2Service.getRedirectUri(oauth2Client, "http://localhost:8080");

        assertThat(result).isEqualTo(
            "https://host/authorizationPath?client_id=clientId&redirect_uri=http://localhost:8080&response_type=code&scope=scope");
    }

    @Test
    void 정상적으로_로그인_처리를_한다() {
        Oauth2Client oauth2Client = Oauth2ClientFixture.oauth2Client;
        String code = "code";
        String requestUri = "http://localhost:8080";
        given(oauthRequester.requestProfile(oauth2Client, code, requestUri)).willReturn(
            new ProviderProfile("providerId", ProviderType.KAKAO));
        given(signupService.registerAndLogin("providerId", ProviderType.KAKAO.name()))
            .willReturn(new EffectiveProfile(1L));
        given(tokenProvider.createToken(1L))
            .willReturn(new TokenResponse(1L, "accessToken", "refreshToken"));

        TokenResponse result = oauth2Service.login(oauth2Client, code, requestUri);

        assertSoftly(softly -> {
            softly.assertThat(result.memberId()).isEqualTo(1L);
            softly.assertThat(result.accessToken()).isEqualTo("accessToken");
            softly.assertThat(result.refreshToken()).isEqualTo("refreshToken");
        });
    }
}
