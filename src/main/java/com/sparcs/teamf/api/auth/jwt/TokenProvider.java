package com.sparcs.teamf.api.auth.jwt;

import com.sparcs.teamf.api.auth.dto.EffectiveMember;
import com.sparcs.teamf.api.auth.dto.OneTimeTokenResponse;
import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.exception.RefreshTokenValidationException;
import com.sparcs.teamf.domain.token.AccessToken;
import com.sparcs.teamf.domain.token.AccessTokenRepository;
import com.sparcs.teamf.domain.token.UserToken;
import com.sparcs.teamf.domain.token.UserTokenRepository;
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
    private final long oneTimeTokenValidityInSeconds;
    private final AccessTokenRepository accessTokenRepository;
    private final UserTokenRepository userTokenRepository;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
                         @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
                         @Value("${jwt.one-time-token-validity-in-seconds}") long oneTimeTokenValidityInSeconds,
                         AccessTokenRepository accessTokenRepository,
                         UserTokenRepository userTokenRepository
    ) {
        this.secret = secret;
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds * 1000;
        this.oneTimeTokenValidityInSeconds = oneTimeTokenValidityInSeconds * 1000;
        this.accessTokenRepository = accessTokenRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse createToken(Long memberId, String email) {
        String accessToken = buildTokenWithClaims(memberId, email, accessTokenValidityInSeconds);
        String refreshToken = buildTokenWithClaims(memberId, email, refreshTokenValidityInSeconds);
        UserToken userToken = new UserToken(memberId, refreshToken);
        AccessToken accessTokenEntity = new AccessToken(memberId, accessToken);
        userTokenRepository.save(userToken);
        accessTokenRepository.save(accessTokenEntity);
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
        if (!validateRefreshToken(refreshToken)) {
            throw new RefreshTokenValidationException();
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        Long memberId = claims.get(MEMBER_ID, Long.class);
        String email = claims.getSubject();
        userTokenRepository.deleteById(refreshToken);
        return createToken(memberId, email);
    }

    public OneTimeTokenResponse createOneTimeToken(Long memberId, String email) {
        String oneTimeToken = buildTokenWithClaims(memberId, email, oneTimeTokenValidityInSeconds);
        return new OneTimeTokenResponse(oneTimeToken);
    }

    boolean validateAccessToken(String accessToken) {
        if (!validateToken(accessToken)) {
            return false;
        }
        return accessTokenRepository.existsById(accessToken);
    }

    boolean validateRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            return false;
        }
        return accessTokenRepository.existsById(refreshToken);
    }


    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
        }
        log.info(token);
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

    public void deleteRefreshToken(String refreshToken) {
        userTokenRepository.deleteById(refreshToken);
    }

    public void deleteAccessToken(String accessToken) {
        accessTokenRepository.deleteById(accessToken);
    }
}
