package com.sparcs.teamf.service;

import com.sparcs.teamf.emailauth.EmailAuth;
import com.sparcs.teamf.emailauth.EmailAuthRepository;
import com.sparcs.teamf.emailauth.Event;
import com.sparcs.teamf.exception.DuplicateEmailException;
import com.sparcs.teamf.exception.EmailRequestRequiredException;
import com.sparcs.teamf.exception.PasswordMismatchException;
import com.sparcs.teamf.exception.UnverifiedEmailException;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.nickname.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    private final NicknameGenerator nicknameGenerator;

    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        validateAlreadyRegistered(email);
        handleUnverifiedEmail(email);

        Member member = Member.of(nicknameGenerator.generate(), email, passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    private void validateAlreadyRegistered(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void handleUnverifiedEmail(String email) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email,
                        Event.REGISTRATION)
                .orElseThrow(EmailRequestRequiredException::new);
        if (!emailAuth.getIsAuthenticated()) {
            throw new UnverifiedEmailException();
        }
    }
}
