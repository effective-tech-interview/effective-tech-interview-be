package com.sparcs.teamf.api.auth.service;

import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.jwt.TokenProvider;
import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.signup.exception.PasswordMismatchException;
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
}
