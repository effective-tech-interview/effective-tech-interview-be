package com.sparcs.teamf.api.auth.service;

import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.jwt.TokenProvider;
import com.sparcs.teamf.api.emailauth.error.VerificationCodeMismatchException;
import com.sparcs.teamf.api.member.error.MemberNotFoundException;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
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
        verifyPassword(password, member.getPassword());

        TokenResponse tokenResponse = resolveToken(member.getId(), member.getEmail());
        return new TokenResponse(member.getId(), tokenResponse.accessToken(), tokenResponse.refreshToken());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    private void verifyPassword(String inputPassword, String savedPassword) {
        if (!passwordEncoder.matches(inputPassword, savedPassword)) {
            throw new VerificationCodeMismatchException();
        }
    }

    private TokenResponse resolveToken(Long memberId, String email) {
        return tokenProvider.createToken(memberId, email);
    }
}