package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.AccessTokenResponse;
import com.sparcs.teamf.api.auth.dto.AuthenticateEmailRequest;
import com.sparcs.teamf.api.auth.dto.LoginRequest;
import com.sparcs.teamf.api.auth.dto.ResetPasswordEmailRequest;
import com.sparcs.teamf.api.auth.dto.SendEmailRequest;
import com.sparcs.teamf.dto.OneTimeTokenResponse;
import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.jwt.TokenUtil;
import com.sparcs.teamf.service.AuthService;
import com.sparcs.teamf.service.EmailAuthService;
import com.sparcs.teamf.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Login")
@RequestMapping("v1/members")
public class LoginController {

    private final MemberService memberService;
    private final AuthService authService;
    private final EmailAuthService emailAuthService;
    private final TokenUtil tokenUtil;

    @PostMapping("/password-reset")
    @Operation(summary = "비밀번호 재설정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
        @ApiResponse(responseCode = "400", description = "bad request"),
        @ApiResponse(responseCode = "401", description = "invalid one time token"),
        @ApiResponse(responseCode = "403", description = "forbidden"),
        @ApiResponse(responseCode = "404", description = "not found")})
    @SecurityRequirements
    public void resetPassword(@RequestBody @Valid ResetPasswordEmailRequest request) {
        memberService.resetPassword(request.email(), request.password(), request.confirmPassword());
    }

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
        tokenUtil.setRefreshTokenInCookie(httpServletResponse, tokenResponse.refreshToken());
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
        tokenUtil.setRefreshTokenInCookie(httpServletResponse, tokenResponse.refreshToken());
        return new AccessTokenResponse(tokenResponse.memberId(), tokenResponse.accessToken());
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
}
