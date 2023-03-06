package com.sparcs.teamf.api.emailauth.service;

import static com.sparcs.teamf.domain.emailauth.Event.REGISTRATION;
import static com.sparcs.teamf.domain.emailauth.Event.RESET_PASSWORD;

import com.sparcs.teamf.api.emailauth.exception.EmailRequestRequiredException;
import com.sparcs.teamf.api.emailauth.exception.UnverifiedEmailException;
import com.sparcs.teamf.api.emailauth.exception.VerificationCodeMismatchException;
import com.sparcs.teamf.api.member.exception.DuplicateEmailException;
import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.signup.exception.PasswordMismatchException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
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

    public void verifyPasswordResetCode(String email, Integer inputVerificationCode) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email, RESET_PASSWORD)
                .orElseThrow(EmailRequestRequiredException::new);

        verifyVerificationCodeMismatch(emailAuth.getVerificationCode(), inputVerificationCode);
        emailAuth.authenticate();
    }

    public void resetPassword(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        handleUnverifiedEmail(email);
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        member.updatePassword(passwordEncoder.encode(password));
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

    private void handleUnverifiedEmail(String email) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email, RESET_PASSWORD)
                .orElseThrow(EmailRequestRequiredException::new);
        if (!emailAuth.getIsAuthenticated()) {
            throw new UnverifiedEmailException();
        }
    }
}
