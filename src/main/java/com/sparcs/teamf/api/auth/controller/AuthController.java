package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.LoginRequest;
import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.service.AuthService;
import com.sparcs.teamf.api.emailauth.dto.AuthenticateEmailRequest;
import com.sparcs.teamf.api.emailauth.dto.SendEmailRequest;
import com.sparcs.teamf.api.emailauth.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final AuthService authService;
    private final EmailAuthService emailAuthService;

    @PostMapping("login")
    @Operation(summary = "로그인")
    public void login(@RequestBody @Valid LoginRequest request, HttpServletResponse httpServletResponse) {

        TokenResponse token = authService.login(request.email(), request.password());
        httpServletResponse.addHeader("Authorization", "Bearer " + token.accessToken());
        httpServletResponse.addHeader("X-Refresh-Token", token.refreshToken());
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
