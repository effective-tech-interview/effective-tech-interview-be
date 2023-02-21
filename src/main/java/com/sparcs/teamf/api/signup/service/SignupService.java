package com.sparcs.teamf.api.signup.service;

import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.jwt.TokenProvider;
import com.sparcs.teamf.api.emailauth.error.EmailRequestRequiredException;
import com.sparcs.teamf.api.emailauth.error.UnverifiedEmailException;
import com.sparcs.teamf.api.member.error.DuplicateEmailException;
import com.sparcs.teamf.api.signup.config.NicknameGenerator;
import com.sparcs.teamf.api.signup.error.PasswordMismatchException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final NicknameGenerator nicknameGenerator;
    private final TokenProvider tokenProvider;

    public TokenResponse signup(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        checkEmailAlreadyRegistered(email);
        checkEmailVerified(email);

        Member member = Member.of(generateRandomNickname(), email, passwordEncoder.encode(password));
        Member savedMember = memberRepository.save(member);
        return tokenProvider.createToken(savedMember.getId(), savedMember.getEmail());
    }

    private void checkEmailAlreadyRegistered(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void checkEmailVerified(String email) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailOrderByCreatedDateDesc(email)
                .orElseThrow(EmailRequestRequiredException::new);
        if (!emailAuth.getIsAuthenticated()) {
            throw new UnverifiedEmailException();
        }
    }

    private String generateRandomNickname() {
        return nicknameGenerator.generate();
    }
}
