package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.LoginRequest;
import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.service.AuthService;
import com.sparcs.teamf.api.emailauth.dto.AuthenticateEmailRequest;
import com.sparcs.teamf.api.emailauth.dto.SendEmailRequest;
import com.sparcs.teamf.api.emailauth.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailAuthService emailAuthService;

    @PostMapping("login")
    @Operation(summary = "로그인")
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request.email(), request.password());
    }

    @PostMapping("/email/send")
    @Operation(summary = "이메일 인증 코드 전송")
    public void sendEmailForSignup(@RequestBody @Valid SendEmailRequest request) {
        emailAuthService.sendEmailForSignup(request.email());
    }

    @PostMapping("/email/authenticate")
    @Operation(summary = "이메일 인증 코드 확인")
    public void authenticateEmailForSignup(@RequestBody @Valid AuthenticateEmailRequest request) {
        emailAuthService.authenticateEmailForSignup(request.email(), request.verificationCode());
    }
}
