package com.sparcs.teamf.api.emailauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class SignupEmailService implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String email, int verificationCode) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("회원가입 이메일 인증");
        simpleMailMessage.setText(generateEmailContent(verificationCode));
        javaMailSender.send(simpleMailMessage);
    }

    private String generateEmailContent(int emailVerificationCode) {
        return "인증 코드는 " + emailVerificationCode + "입니다. \n"
                + "해당 인증 코드를 입력해주세요.";
    }
}
