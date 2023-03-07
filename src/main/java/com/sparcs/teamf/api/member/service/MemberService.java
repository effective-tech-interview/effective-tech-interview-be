package com.sparcs.teamf.api.member.service;

import static com.sparcs.teamf.domain.emailauth.Event.RESET_PASSWORD;

import com.sparcs.teamf.api.emailauth.exception.EmailRequestRequiredException;
import com.sparcs.teamf.api.emailauth.exception.UnverifiedEmailException;
import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.signup.exception.PasswordMismatchException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        handleUnverifiedEmail(email);
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        member.updatePassword(passwordEncoder.encode(password));
    }

    private void handleUnverifiedEmail(String email) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email, RESET_PASSWORD)
                .orElseThrow(EmailRequestRequiredException::new);
        if (!emailAuth.getIsAuthenticated()) {
            throw new UnverifiedEmailException();
        }
    }
}
