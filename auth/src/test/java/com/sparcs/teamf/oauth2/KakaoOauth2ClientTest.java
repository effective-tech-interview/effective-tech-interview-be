package com.sparcs.teamf.oauth2;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("카카오 클라이언트 정보를")
class KakaoOauth2ClientTest {

    @Test
    void 정상적으로_생성할_수_있다() {
        KakaoOauth2Client kakaoOauth2Client = new KakaoOauth2Client("clientId", "clientSecret", "scope", "redirectUri");
        Oauth2Client oauth2Client = kakaoOauth2Client.getClient();

        assertSoftly(
            softly -> {
                assertNotNull(oauth2Client);
                softly.assertThat(oauth2Client.getClientId()).isEqualTo("clientId");
                softly.assertThat(oauth2Client.getClientSecret()).isEqualTo("clientSecret");
                softly.assertThat(oauth2Client.getScope()).isEqualTo("scope");
                softly.assertThat(oauth2Client.getRedirectUri()).isEqualTo("redirectUri");
            }
        );
    }
}
