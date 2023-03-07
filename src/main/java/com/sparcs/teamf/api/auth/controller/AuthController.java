package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.LoginRequest;
import com.sparcs.teamf.api.auth.dto.OneTimeTokenResponse;
import com.sparcs.teamf.api.auth.dto.ResetPasswordEmailRequest;
import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.auth.service.AuthService;
import com.sparcs.teamf.api.emailauth.dto.AuthenticateEmailRequest;
import com.sparcs.teamf.api.emailauth.dto.SendEmailRequest;
import com.sparcs.teamf.api.emailauth.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request.email(), request.password());
    }

    @PostMapping("/refresh")
    @Operation(summary = "엑세스 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "invalid refresh token", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public TokenResponse refresh(@RequestHeader(value = "Authorization") String refreshToken) {
        return authService.refresh(getTokenFromHeader(refreshToken));
    }

    @PostMapping("/email/send")
    @Operation(summary = "이메일 인증 코드 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "409", description = "the email is already registered")})
    public void sendEmailForSignup(@RequestBody @Valid SendEmailRequest request) {
        emailAuthService.sendEmailForSignup(request.email());
    }

    @PostMapping("/email/authenticate")
    @Operation(summary = "이메일 인증 코드 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "422", description = "invalid verification code")})
    public void authenticateEmailForSignup(@RequestBody @Valid AuthenticateEmailRequest request) {
        emailAuthService.authenticateEmailForSignup(request.email(), request.verificationCode());
    }

    @PostMapping("/password-reset/email")
    @Operation(summary = "비밀번호 재설정 인증 코드 전송")
    public void sendPasswordResetCode(@RequestBody @Valid SendEmailRequest request) {
        emailAuthService.sendPasswordResetCode(request.email());
    }

    @PostMapping("/password-reset/email-verification")
    @Operation(summary = "비밀번호 재설정 인증 코드 검증")
    public OneTimeTokenResponse verifyPasswordResetCode(@RequestBody @Valid AuthenticateEmailRequest request) {
        return emailAuthService.verifyPasswordResetCode(request.email(), request.verificationCode());
    }

    @PostMapping("/password-reset/reset")
    @Operation(summary = "비밀번호 재설정")
    public void resetPassword(@RequestBody @Valid ResetPasswordEmailRequest request) {
        emailAuthService.resetPassword(request.email(), request.password(), request.confirmPassword());
    }

    private String getTokenFromHeader(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
