package com.sparcs.teamf.service;

import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.exception.MemberNotFoundException;
import com.sparcs.teamf.exception.PasswordMismatchException;
import com.sparcs.teamf.jwt.TokenProvider;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public TokenResponse login(String email, String password) {
        Member member = getMemberByEmail(email);
        validatePassword(password, member.getPassword());
        return tokenProvider.createToken(member.getId(), member.getEmail());
    }

    public TokenResponse refresh(String refreshToken) {
        TokenResponse tokenResponse = tokenProvider.reissueToken(refreshToken);
        validateMemberId(tokenResponse.memberId());
        return tokenResponse;
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    private void validatePassword(String inputPassword, String savedPassword) {
        if (!passwordEncoder.matches(inputPassword, savedPassword)) {
            throw new PasswordMismatchException();
        }
    }

    private void validateMemberId(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
    }

    public TokenResponse getFreeToken(long memberId, String email) {
        return tokenProvider.createToken(memberId, email);
    }

    public void logout(String accessToken, String refreshToken) {
        tokenProvider.deleteAccessToken(accessToken);
        tokenProvider.deleteRefreshToken(refreshToken);
    }
}
