package com.sparcs.teamf.api.signup.service;

import com.sparcs.teamf.api.emailauth.exception.EmailRequestRequiredException;
import com.sparcs.teamf.api.emailauth.exception.UnverifiedEmailException;
import com.sparcs.teamf.api.member.exception.DuplicateEmailException;
import com.sparcs.teamf.api.signup.config.NicknameGenerator;
import com.sparcs.teamf.api.signup.exception.PasswordMismatchException;
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

    public void signup(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        validateAlreadyRegistered(email);
        handleUnverifiedEmail(email);

        Member member = Member.of(generateRandomNickname(), email, passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    private void validateAlreadyRegistered(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void handleUnverifiedEmail(String email) {
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
