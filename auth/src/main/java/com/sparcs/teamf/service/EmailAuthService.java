package com.sparcs.teamf.service;

import com.sparcs.teamf.dto.OneTimeTokenResponse;
import com.sparcs.teamf.emailauth.EmailAuth;
import com.sparcs.teamf.emailauth.EmailAuthRepository;
import com.sparcs.teamf.emailauth.Event;
import com.sparcs.teamf.exception.DuplicateEmailException;
import com.sparcs.teamf.exception.EmailRequestRequiredException;
import com.sparcs.teamf.exception.MemberNotFoundException;
import com.sparcs.teamf.exception.VerificationCodeMismatchException;
import com.sparcs.teamf.jwt.TokenProvider;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailAuthService {

    @Resource(name = "signupEmailService")
    private final EmailService signupEmailService;

    @Resource(name = "resetPasswordEmailService")
    private final EmailService resetPasswordEmailService;

    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public void sendEmailForSignup(String email) {
        if (isAlreadyRegistered(email)) {
            throw new DuplicateEmailException();
        }
        int verificationCode = generateVerificationCode();
        signupEmailService.send(email, verificationCode);
        emailAuthRepository.save(EmailAuth.of(email, Event.REGISTRATION, verificationCode));
    }

    public void authenticateEmailForSignup(String email, int inputVerificationCode) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email,
                        Event.REGISTRATION)
                .orElseThrow(EmailRequestRequiredException::new);

        verifyVerificationCodeMismatch(emailAuth.getVerificationCode(), inputVerificationCode);
        emailAuth.authenticate();
    }

    public void sendPasswordResetCode(String email) {
        if (!isAlreadyRegistered(email)) {
            throw new MemberNotFoundException();
        }
        int verificationCode = generateVerificationCode();
        resetPasswordEmailService.send(email, verificationCode);
        emailAuthRepository.save(EmailAuth.of(email, Event.RESET_PASSWORD, verificationCode));
    }

    public OneTimeTokenResponse verifyPasswordResetCode(String email, Integer inputVerificationCode) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email,
                        Event.RESET_PASSWORD)
                .orElseThrow(EmailRequestRequiredException::new);
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        verifyVerificationCodeMismatch(emailAuth.getVerificationCode(), inputVerificationCode);
        emailAuth.authenticate();
        return tokenProvider.createOneTimeToken(member.getId(), email);
    }

    private boolean isAlreadyRegistered(String email) {
        return memberRepository.existsByEmail(email);
    }

    private int generateVerificationCode() {
        return random.nextInt(100000, 999999);
    }

    private void verifyVerificationCodeMismatch(int inputVerificationCode, int savedVerificationCode) {
        if (inputVerificationCode != savedVerificationCode) {
            throw new VerificationCodeMismatchException();
        }
    }
}
