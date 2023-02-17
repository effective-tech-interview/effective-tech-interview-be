package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.AuthenticateEmailRequest;
import com.sparcs.teamf.api.auth.dto.SendEmailRequest;
import com.sparcs.teamf.api.emailauth.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailAuthService emailAuthService;

    @PostMapping("/email/send")
    @Operation(summary = "이메일 인증 코드 전송")
    public void sendEmailForSignup(@RequestBody @Valid SendEmailRequest request) {
        emailAuthService.sendEmailForSignup(request.email());
    }

    @GetMapping("/email/authenticate")
    public void authenticateEmailForSignup(@RequestBody @Valid AuthenticateEmailRequest request) {
        emailAuthService.authenticateEmailForSignup(request.email(), Integer.parseInt(request.verificationCode()));
    }
}
