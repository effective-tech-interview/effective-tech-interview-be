package com.sparcs.teamf.api.emailauth;

import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailService emailService;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;

    public void sendEmailForSignup(String email) {
        verifyEmail(email);

        int verificationCode = emailService.send(email);
        emailAuthRepository.save(EmailAuth.of(email, verificationCode));
    }

    private void verifyEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalStateException();
        }
    }
}
