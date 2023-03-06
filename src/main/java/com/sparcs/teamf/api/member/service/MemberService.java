package com.sparcs.teamf.api.member.service;

import static com.sparcs.teamf.domain.emailauth.Event.RESET_PASSWORD;

import com.sparcs.teamf.api.emailauth.service.EmailService;
import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.MemberRepository;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Resource(name = "resetPasswordEmailService")
    private final EmailService resetPasswordEmailService;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public void sendPasswordResetCode(String email) {
        if (!isAlreadyRegistered(email)) {
            throw new MemberNotFoundException();
        }
        int verificationCode = generateVerificationCode();
        resetPasswordEmailService.send(email, verificationCode);
        emailAuthRepository.save(EmailAuth.of(email, RESET_PASSWORD, verificationCode));
    }

    private int generateVerificationCode() {
        return random.nextInt(100000, 999999);
    }

    private boolean isAlreadyRegistered(String email) {
        return memberRepository.existsByEmail(email);
    }
}
