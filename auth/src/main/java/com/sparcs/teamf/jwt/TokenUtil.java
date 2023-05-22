package com.sparcs.teamf.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private static final String MEMBER_ID = "memberId";

    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;
    private final long oneTimeTokenValidityInSeconds;
    private final Key key;

    public TokenUtil(@Value("${jwt.secret}") String secret,
                     @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
                     @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
                     @Value("${jwt.one-time-token-validity-in-seconds}") long oneTimeTokenValidityInSeconds
    ) {
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds * 1000;
        this.oneTimeTokenValidityInSeconds = oneTimeTokenValidityInSeconds * 1000;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
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
}
