package com.sparcs.teamf.api.emailauth.service;

import com.sparcs.teamf.api.emailauth.dto.AuthenticateEmailResponse;
import com.sparcs.teamf.api.emailauth.error.InvalidEmailOrVerificationCodeException;
import com.sparcs.teamf.api.emailauth.error.VerificationCodeMismatchException;
import com.sparcs.teamf.api.member.error.DuplicateEmailException;
import com.sparcs.teamf.domain.emailauth.EmailAuth;
import com.sparcs.teamf.domain.emailauth.EmailAuthRepository;
import com.sparcs.teamf.domain.member.MemberRepository;
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

    public void sendEmailForSignup(String email) {
        isEmailAlreadyRegistered(email);

        int verificationCode = emailService.send(email);
        emailAuthRepository.save(EmailAuth.of(email, verificationCode));
    }

    private void isEmailAlreadyRegistered(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    public AuthenticateEmailResponse authenticateEmailForSignup(String email, int inputVerificationCode) {
        EmailAuth latestEmailAuth = emailAuthRepository.findFirstByEmailOrderByCreatedDateDesc(email)
                .filter(emailAuth -> !emailAuth.getIsAuthenticated())
                .orElseThrow(InvalidEmailOrVerificationCodeException::new);

        if (inputVerificationCode != latestEmailAuth.getVerificationCode()) {
            throw new VerificationCodeMismatchException();
        }
        latestEmailAuth.authenticate();
        return new AuthenticateEmailResponse("인증에 성공했습니다.");
    }
}
