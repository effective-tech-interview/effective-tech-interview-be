package com.sparcs.teamf.api.emailauth.service;

import static com.sparcs.teamf.domain.emailauth.Event.REGISTRATION;

import com.sparcs.teamf.api.emailauth.exception.EmailRequestRequiredException;
import com.sparcs.teamf.api.emailauth.exception.VerificationCodeMismatchException;
import com.sparcs.teamf.api.member.exception.DuplicateEmailException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.MemberRepository;
import java.util.concurrent.ThreadLocalRandom;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailService emailService;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public void sendEmailForSignup(String email) {
        validateAlreadyRegistered(email);

        int verificationCode = generateVerificationCode();
        emailService.send(email, verificationCode);
        emailAuthRepository.save(EmailAuth.of(email, REGISTRATION, verificationCode));
    }

    public void authenticateEmailForSignup(String email, int inputVerificationCode) {
        EmailAuth emailAuth = emailAuthRepository.findFirstByEmailAndEventOrderByCreatedDateDesc(email, REGISTRATION)
                .orElseThrow(EmailRequestRequiredException::new);

        verifyVerificationCodeMismatch(emailAuth.getVerificationCode(), inputVerificationCode);
        emailAuth.authenticate();
    }

    private void validateAlreadyRegistered(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
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
