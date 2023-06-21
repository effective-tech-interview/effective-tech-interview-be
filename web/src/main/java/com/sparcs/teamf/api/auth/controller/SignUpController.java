package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.*;
import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.jwt.TokenUtil;
import com.sparcs.teamf.oauth2.kakao.KakaoOauth2Client;
import com.sparcs.teamf.service.EmailAuthService;
import com.sparcs.teamf.service.Oauth2Service;
import com.sparcs.teamf.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "Signup")
@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
@SecurityRequirements
public class SignUpController {

    private final SignupService signupService;
    private final Oauth2Service oauth2Service;
    private final KakaoOauth2Client kakaoOauth2Client;
    private final EmailAuthService emailAuthService;
    private final TokenUtil tokenUtil;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "403", description = "forbidden"),
            @ApiResponse(responseCode = "409", description = "the email is already registered")})
    public void signup(@RequestBody @Valid SignupRequest request) {
        signupService.signup(request.email(), request.password(), request.confirmPassword());
    }

    @GetMapping("/signup/oauth2/kakao")
    @Operation(summary = "카카오 로그인")
    public void kakaoRedirect(HttpServletResponse response, String redirectUri)
            throws IOException {
        String result = oauth2Service.getRedirectUri(kakaoOauth2Client.getClient(), redirectUri);
        response.sendRedirect(result);
    }

    @PostMapping("/signup/oauth2/kakao/code")
    @Operation(summary = "카카오 로그인 인가 코드를 받아서, 액세스 토큰을 발급해준다")
    public ResponseEntity<AccessTokenResponse> kakaoLogin(@RequestBody @Valid Oauth2CodeRequest oauth2CodeRequest,
                                                          HttpServletResponse response) {
        TokenResponse tokenResponse = oauth2Service.login(kakaoOauth2Client.getClient(), oauth2CodeRequest.code(),
                oauth2CodeRequest.redirectUri());
        tokenUtil.setRefreshTokenInCookie(response, tokenResponse.refreshToken());
        return ResponseEntity.ok(new AccessTokenResponse(tokenResponse.memberId(), tokenResponse.accessToken()));
    }

    @PostMapping("/auth/email/send")
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

    @PostMapping("/auth/email/authenticate")
    @Operation(summary = "회원가입 이메일 인증 코드 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "422", description = "invalid verification code")})
    public void authenticateEmailForSignup(@RequestBody @Valid AuthenticateEmailRequest request) {
        emailAuthService.authenticateEmailForSignup(request.email(), request.verificationCode());
    }
}
