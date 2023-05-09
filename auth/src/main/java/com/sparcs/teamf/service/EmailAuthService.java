package com.sparcs.teamf.service;

import com.sparcs.teamf.dto.OneTimeTokenResponse;
import com.sparcs.teamf.email.AuthEmailSender;
import com.sparcs.teamf.email.EmailAuthentication;
import com.sparcs.teamf.email.EmailAuthenticationRepository;
import com.sparcs.teamf.email.EmailKeyBuilder;
import com.sparcs.teamf.email.Event;
import com.sparcs.teamf.exception.DuplicateEmailException;
import com.sparcs.teamf.exception.EmailSendLimitExceededException;
import com.sparcs.teamf.exception.EmailVerificationNotFoundException;
import com.sparcs.teamf.exception.MemberNotFoundException;
import com.sparcs.teamf.exception.VerificationCodeMismatchException;
import com.sparcs.teamf.jwt.TokenProvider;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import java.util.concurrent.ThreadLocalRandom;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailAuthService {

    private final AuthEmailSender authEmailSender;
    private final EmailKeyBuilder emailKeyBuilder;
    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public void sendEmailForSignup(String email) {
        if (isAlreadyRegistered(email)) {
            throw new DuplicateEmailException();
        }
        String emailKey = emailKeyBuilder.generate(email, Event.REGISTRATION);
        checkIfEmailAlreadySent(emailKey);

        int verificationCode = generateVerificationCode();
        authEmailSender.sendSignupEmail(email, verificationCode);
        emailAuthenticationRepository.save(new EmailAuthentication(emailKey, verificationCode));
    }

    public void authenticateEmailForSignup(String email, int inputVerificationCode) {
        EmailAuthentication emailAuth = emailAuthenticationRepository.findById(
            emailKeyBuilder.generate(email, Event.REGISTRATION)).orElseThrow(EmailVerificationNotFoundException::new);

        verifyVerificationCodeMismatch(emailAuth.getVerificationCode(), inputVerificationCode);
        emailAuth.authenticate();
        emailAuthenticationRepository.save(emailAuth);
    }

    public void sendPasswordResetCode(String email) {
        if (!isAlreadyRegistered(email)) {
            throw new MemberNotFoundException();
        }
        String emailKey = emailKeyBuilder.generate(email, Event.RESET_PASSWORD);
        checkIfEmailAlreadySent(emailKey);

        int verificationCode = generateVerificationCode();
        authEmailSender.sendRegisterEmail(email, verificationCode);
        emailAuthenticationRepository.save(new EmailAuthentication(emailKey, verificationCode));
    }

    public OneTimeTokenResponse verifyPasswordResetCode(String email, Integer inputVerificationCode) {
        EmailAuthentication emailAuth = emailAuthenticationRepository.findById(
            emailKeyBuilder.generate(email, Event.RESET_PASSWORD)).orElseThrow(EmailVerificationNotFoundException::new);
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

    private void checkIfEmailAlreadySent(String emailKey) {
        if (emailAuthenticationRepository.existsById(emailKey)) {
            throw new EmailSendLimitExceededException();
        }
    }

    private void verifyVerificationCodeMismatch(int inputVerificationCode, int savedVerificationCode) {
        if (inputVerificationCode != savedVerificationCode) {
            throw new VerificationCodeMismatchException();
        }
    }
}
