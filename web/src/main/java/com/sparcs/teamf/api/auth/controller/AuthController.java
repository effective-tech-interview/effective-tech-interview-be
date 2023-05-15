package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.AccessTokenResponse;
import com.sparcs.teamf.api.auth.dto.AuthenticateEmailRequest;
import com.sparcs.teamf.api.auth.dto.LoginRequest;
import com.sparcs.teamf.api.auth.dto.SendEmailRequest;
import com.sparcs.teamf.dto.OneTimeTokenResponse;
import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.service.AuthService;
import com.sparcs.teamf.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
@SecurityRequirements
public class AuthController {

    private final AuthService authService;
    private final EmailAuthService emailAuthService;

    @PostMapping("login")
    @Operation(summary = "로그인")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccessTokenResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public AccessTokenResponse login(@RequestBody @Valid LoginRequest request,
                                     HttpServletResponse httpServletResponse) {
        TokenResponse tokenResponse = authService.login(request.email(), request.password());
        setRefreshToken(httpServletResponse, tokenResponse.refreshToken());
        return new AccessTokenResponse(tokenResponse.memberId(), tokenResponse.accessToken());
    }

    @PostMapping("/refresh")
    @Operation(summary = "엑세스 토큰 재발급")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccessTokenResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
        @ApiResponse(responseCode = "401", description = "invalid refresh token", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public AccessTokenResponse refresh(@CookieValue("refreshToken") String refreshToken,
                                       HttpServletResponse httpServletResponse) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        setRefreshToken(httpServletResponse, tokenResponse.refreshToken());
        return new AccessTokenResponse(tokenResponse.memberId(), tokenResponse.accessToken());
    }

    @PostMapping("/email/send")
    @Operation(summary = "회원가입 이메일 인증 코드 전송")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
        @ApiResponse(responseCode = "400", description = "bad request"),
        @ApiResponse(responseCode = "409", description = "the email is already registered"),
        @ApiResponse(responseCode = "429", description = "too many requests")})
    public void sendEmailForSignup(@RequestBody @Valid SendEmailRequest request) {
        emailAuthService.sendEmailForSignup(request.email());
    }

    @PostMapping("/email/authenticate")
    @Operation(summary = "회원가입 이메일 인증 코드 확인")
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
        @ApiResponse(responseCode = "400", description = "bad request"),
        @ApiResponse(responseCode = "404", description = "not found"),
        @ApiResponse(responseCode = "429", description = "too many requests")})
    public void sendPasswordResetCode(@RequestBody @Valid SendEmailRequest request) {
        emailAuthService.sendPasswordResetCode(request.email());
    }

    @PostMapping("/password-reset/email-verification")
    @Operation(summary = "비밀번호 재설정 인증 코드 검증")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OneTimeTokenResponse.class))}),
        @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
        @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public OneTimeTokenResponse verifyPasswordResetCode(@RequestBody @Valid AuthenticateEmailRequest request) {
        return emailAuthService.verifyPasswordResetCode(request.email(), request.verificationCode());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(value = "Authorization") String accessToken,
                       @CookieValue(value = "refreshToken") String refreshToken) {
        authService.logout(getTokenFromHeader(accessToken), refreshToken);
    }

    private String getTokenFromHeader(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    private void setRefreshToken(HttpServletResponse httpServletResponse, String refreshToken) {
        //expire after 1 week
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        httpServletResponse.setHeader("Set-Cookie",
            "refreshToken=" + refreshToken + "; Path=/; HttpOnly; SameSite=None; Secure; expires=" + date);
    }

    @GetMapping("/test")
    public String test(HttpServletResponse response) {
        //expire after 3 days
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3);
        response.setHeader("Set-Cookie",
            "refreshToken=1234; Path=/; HttpOnly; SameSite=None; Secure; expires=" + date);
        return "test";
    }

    @GetMapping("/test2")
    public String cookieTest(@CookieValue(value = "refreshToken") String refreshToken) {
        return refreshToken;
    }
}
