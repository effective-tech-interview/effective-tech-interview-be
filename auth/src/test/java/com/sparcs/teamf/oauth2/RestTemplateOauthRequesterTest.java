package com.sparcs.teamf.oauth2;

import static com.sparcs.teamf.oauth2.ProviderToken.ProviderTokenFactory.create;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.sparcs.teamf.jwt.TokenUtil;
import com.sparcs.teamf.member.ProviderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class RestTemplateOauthRequesterTest {

    @InjectMocks
    private RestTemplateOauthRequester restTemplateOauthRequester;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void 프로필_가져오는_기능이_정상적으로_작동한다() {
        ProviderToken providerToken = create("accessToken", "refreshToken", "idToken");
        given(restTemplate.postForObject(anyString(), any(), any())).willReturn(providerToken);
        given(tokenUtil.getProviderId("idToken")).willReturn("providerId");

        ProviderProfile result = restTemplateOauthRequester.requestProfile(Oauth2ClientFixture.oauth2Client, "code",
            "requestUri");

        assertSoftly(softly -> {
                softly.assertThat(result.providerId()).isEqualTo("providerId");
                softly.assertThat(result.provider()).isEqualTo(ProviderType.KAKAO);
            }
        );
    }
}
