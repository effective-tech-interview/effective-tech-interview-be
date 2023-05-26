package com.sparcs.teamf.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

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

    @Test
    void 리프레시_토큰을_쿠키에_잘_설정한다() {
        HttpServletResponse response = new MockHttpServletResponse();
        tokenUtil.setRefreshTokenInCookie(response, "refreshToken");

        String result = response.getHeader("Set-Cookie");
        assertSoftly(softly -> {
            softly.assertThat(result).contains("refreshToken=refreshToken");
            softly.assertThat(result).contains("Path=/");
            softly.assertThat(result).contains("HttpOnly");
            softly.assertThat(result).contains("Secure");
            softly.assertThat(result).contains("SameSite=None");
            softly.assertThat(result).contains("Max-Age=10000");
        });
    }

    @Test
    void id_토큰에서_id_를_잘_가져온다() {
        String token = "eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI2NzhhODAyMWQwNDkzZjZmMTE1ZTg4MTk4ZGJkYzA0NSIsInN1YiI6IjI4MDE1MDM3NDUiLCJhdXRoX3RpbWUiOjE2ODUxMTcyNjksImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLshqHsnYDsmrAiLCJleHAiOjE2ODUxMzg4NjksImlhdCI6MTY4NTExNzI2OX0.FErq4mvTOUuO-U6wap8aOwd36Df7AjmVCxoMQvuxOaKls2g6nsWeKpg5YJw-ii48z3hs_FoQ3H16KzXG4WGtV5NjhYLxtBkMT1S6oHnOH0WS5e-EPSsJNMTxol50spCGWvX3JgEwfrkiSuqeb1ZKI8zIa4hPc3c_79ZIz7HXn0wux1paG1-cnecjt5Bsc9-NiVW4Ki0hOno-Axyy3_O-KDFuOYQPj9P_OKY5AGa0sPzCMx1PPBQcOsuub2Ov9BTbWXORuju5tvVwsRYjRgle4cXp_FnJcGWpzSFXubSw1l9YEZ20i4FDVGEnZ92UoqHNP5Osatt-V-86PhORVKuqow";

        String result = tokenUtil.getProviderId(token);

        assertThat(result).isEqualTo("2801503745");
    }
}
