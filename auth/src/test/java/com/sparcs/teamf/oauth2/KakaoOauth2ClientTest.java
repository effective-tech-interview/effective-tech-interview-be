package com.sparcs.teamf.oauth2;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sparcs.teamf.member.ProviderType;
import com.sparcs.teamf.oauth2.kakao.KakaoOauth2Client;
import com.sparcs.teamf.oauth2.kakao.KakaoProviderToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("카카오 클라이언트 정보를")
class KakaoOauth2ClientTest {

    @Test
    void 정상적으로_생성할_수_있다() {
        KakaoOauth2Client kakaoOauth2Client = new KakaoOauth2Client("clientId", "clientSecret", "scope", "redirectUri",
            "authorizationPath", "host", "tokenPath");
        Oauth2Client oauth2Client = kakaoOauth2Client.getClient();

        assertSoftly(
            softly -> {
                assertNotNull(oauth2Client);
                softly.assertThat(oauth2Client.clientId()).isEqualTo("clientId");
                softly.assertThat(oauth2Client.clientSecret()).isEqualTo("clientSecret");
                softly.assertThat(oauth2Client.scope()).isEqualTo("scope");
                softly.assertThat(oauth2Client.redirectUri()).isEqualTo("redirectUri");
                softly.assertThat(oauth2Client.authorizationPath()).isEqualTo("authorizationPath");
                softly.assertThat(oauth2Client.host()).isEqualTo("host");
                softly.assertThat(oauth2Client.tokenPath()).isEqualTo("tokenPath");
                softly.assertThat(oauth2Client.type()).isEqualTo(ProviderType.KAKAO);
                softly.assertThat(oauth2Client.providerToken()).isEqualTo(KakaoProviderToken.class);
            }
        );
    }
}
