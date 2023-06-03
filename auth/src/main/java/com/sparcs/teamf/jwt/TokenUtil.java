package com.sparcs.teamf.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparcs.teamf.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private static final String MEMBER_ID = "memberId";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;
    private final long oneTimeTokenValidityInSeconds;
    private final Key key;

    public TokenUtil(JwtConfig jwtConfig) {
        accessTokenValidityInSeconds = jwtConfig.getAccessTokenValidityInSeconds() * 1000;
        refreshTokenValidityInSeconds = jwtConfig.getRefreshTokenValidityInSeconds() * 1000;
        oneTimeTokenValidityInSeconds = jwtConfig.getOneTimeTokenValidityInSeconds() * 1000;
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(long memberId) {
        return createToken(Map.of(MEMBER_ID, memberId), "accessToken",
            Duration.ofSeconds(accessTokenValidityInSeconds));
    }

    public String createRefreshToken(long memberId) {
        return createToken(Map.of(MEMBER_ID, memberId), "refreshToken",
            Duration.ofSeconds(refreshTokenValidityInSeconds));
    }

    public String createOneTimeToken(long memberId) {
        return createToken(Map.of(MEMBER_ID, memberId), "oneTimeToken",
            Duration.ofSeconds(oneTimeTokenValidityInSeconds));
    }

    public String createOneTimeToken(Map<String, Object> claims) {
        return createToken(claims, "oneTimeToken", Duration.ofSeconds(oneTimeTokenValidityInSeconds));
    }

    public String createToken(Map<String, Object> claims, String subject, Duration validity) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + validity.toMillis()))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public Claims parseClaim(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public void setRefreshTokenInCookie(HttpServletResponse response, String token) {
        response.setHeader("Set-Cookie",
            "refreshToken=" + token + "; Path=/; HttpOnly; SameSite=None; Secure; Max-Age="
                + refreshTokenValidityInSeconds);
    }

    public String getProviderId(String token) {
        String[] check = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(check[1]));
        try {
            Map<String, Object> map = objectMapper.readValue(payload, Map.class);
            return map.get("sub").toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
