package com.sparcs.teamf.jwt;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TokenUtilTest {

    private final TokenUtil tokenUtil = new TokenUtil("secret".repeat(20), 10, 10, 10);

    @Test
    void 액세스_토큰이_정상적으로_생성된다() {
        String result = tokenUtil.createAccessToken(1L);

        Claims resultClaim = tokenUtil.parseClaim(result);
        assertSoftly(softly -> {
            softly.assertThat(result).isNotBlank();
            softly.assertThat(resultClaim.get("memberId")).isEqualTo(1);
            softly.assertThat(resultClaim.getSubject()).isEqualTo("accessToken");
        });
    }

    @Test
    void 리프레시_토큰이_정상적으로_생성된다() {
        String result = tokenUtil.createRefreshToken(1L);

        Claims resultClaim = tokenUtil.parseClaim(result);
        assertSoftly(softly -> {
            softly.assertThat(result).isNotBlank();
            softly.assertThat(resultClaim.get("memberId")).isEqualTo(1);
            softly.assertThat(resultClaim.getSubject()).isEqualTo("refreshToken");
        });
    }

    @Test
    void 원타임_토큰이_정상적으로_생성된다() {
        String result = tokenUtil.createOneTimeToken(1);

        Claims resultClaim = tokenUtil.parseClaim(result);
        assertSoftly(softly -> {
            softly.assertThat(result).isNotBlank();
            softly.assertThat(resultClaim.getSubject()).isEqualTo("oneTimeToken");
            softly.assertThat(resultClaim.get("memberId")).isEqualTo(1);
        });
    }

    @Test
    void 토큰이_정상적으로_생성된다() {
        String result = tokenUtil.createToken(Map.of(
            "memberId", 1,
            "name", "name"
        ), "subject", Duration.ofSeconds(100));

        Claims resultClaim = tokenUtil.parseClaim(result);
        assertSoftly(softly -> {
            softly.assertThat(result).isNotBlank();
            softly.assertThat(resultClaim.getSubject()).isEqualTo("subject");
            softly.assertThat(resultClaim.get("memberId")).isEqualTo(1);
            softly.assertThat(resultClaim.get("name")).isEqualTo("name");
        });
    }
}
