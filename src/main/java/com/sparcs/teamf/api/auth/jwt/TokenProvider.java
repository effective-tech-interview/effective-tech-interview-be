package com.sparcs.teamf.api.auth.jwt;

import com.sparcs.teamf.api.auth.dto.EffectiveMember;
import com.sparcs.teamf.api.auth.dto.OneTimeTokenResponse;
import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.exception.RefreshTokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {
    private static final String MEMBER_ID = "memberId";

    private final String secret;
    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;
    private final long oneTimeTokenValidatyInSeconds;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
                         @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
                         @Value("${jwt.one-time-token-validity-in-seconds}") long oneTimeTokenValidityInSeconds) {
        this.secret = secret;
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds * 1000;
        this.oneTimeTokenValidatyInSeconds = oneTimeTokenValidityInSeconds * 1000;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse createToken(Long memberId, String email) {
        String accessToken = buildTokenWithClaims(memberId, email, accessTokenValidityInSeconds);
        String refreshToken = buildTokenWithClaims(memberId, email, refreshTokenValidityInSeconds);
        return new TokenResponse(memberId, accessToken, refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        Long memberId = claims.get(MEMBER_ID, Long.class);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        EffectiveMember principal = new EffectiveMember(memberId, authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    public TokenResponse reissueToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RefreshTokenValidationException();
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        Long memberId = claims.get(MEMBER_ID, Long.class);
        String email = claims.getSubject();
        return createToken(memberId, email);
    }

    public OneTimeTokenResponse createOneTimeToken(Long memberId, String email) {
        String oneTimeToken = buildTokenWithClaims(memberId, email, oneTimeTokenValidatyInSeconds);
        return new OneTimeTokenResponse(oneTimeToken);
    }

    boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private String buildTokenWithClaims(Long memberId, String email, long tokenValidityInSeconds) {
        long now = (new Date()).getTime();
        return Jwts.builder()
                .setSubject(email)
                .claim(MEMBER_ID, memberId)
                .setExpiration(new Date(now + tokenValidityInSeconds))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
