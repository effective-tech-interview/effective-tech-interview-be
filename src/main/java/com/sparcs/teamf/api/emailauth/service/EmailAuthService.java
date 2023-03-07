package com.sparcs.teamf.api.emailauth.service;

import static com.sparcs.teamf.domain.emailauth.Event.REGISTRATION;
import static com.sparcs.teamf.domain.emailauth.Event.RESET_PASSWORD;

import com.sparcs.teamf.api.auth.dto.OneTimeTokenResponse;
import com.sparcs.teamf.api.auth.jwt.TokenProvider;
import com.sparcs.teamf.api.emailauth.exception.EmailRequestRequiredException;
import com.sparcs.teamf.api.emailauth.exception.VerificationCodeMismatchException;
import com.sparcs.teamf.api.member.exception.DuplicateEmailException;
import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
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
        emailAuthRepository.save(EmailAuth.of(email, REGISTRATION, verificationCode));
    }

    public void authenticateEmailForSignup(String email, int inputVerificationCode) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email, REGISTRATION)
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
        emailAuthRepository.save(EmailAuth.of(email, RESET_PASSWORD, verificationCode));
    }

    public OneTimeTokenResponse verifyPasswordResetCode(String email, Integer inputVerificationCode) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email, RESET_PASSWORD)
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
