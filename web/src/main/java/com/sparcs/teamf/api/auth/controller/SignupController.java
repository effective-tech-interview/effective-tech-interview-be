package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.api.auth.dto.AccessTokenResponse;
import com.sparcs.teamf.api.auth.dto.Oauth2CodeRequest;
import com.sparcs.teamf.api.auth.dto.SignupRequest;
import com.sparcs.teamf.dto.TokenResponse;
import com.sparcs.teamf.jwt.TokenUtil;
import com.sparcs.teamf.oauth2.kakao.KakaoOauth2Client;
import com.sparcs.teamf.service.Oauth2Service;
import com.sparcs.teamf.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Signup")
@RestController
@RequestMapping("v1/signup")
@RequiredArgsConstructor
@SecurityRequirements
public class SignupController {

    private final SignupService signupService;
    private final Oauth2Service oauth2Service;
    private final KakaoOauth2Client kakaoOauth2Client;
    private final TokenUtil tokenUtil;

    @PostMapping
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

    @GetMapping("/oauth2/kakao")
    @Operation(summary = "카카오 로그인")
    public void kakaoRedirect(HttpServletResponse response, String redirectUri)
        throws IOException {
        String result = oauth2Service.getRedirectUri(kakaoOauth2Client.getClient(), redirectUri);
        response.sendRedirect(result);
    }

    @PostMapping("/oauth2/kakao/code")
    @Operation(summary = "카카오 로그인 인가 코드를 받아서, 액세스 토큰을 발급해준다")
    public ResponseEntity<AccessTokenResponse> kakaoLogin(@RequestBody @Valid Oauth2CodeRequest oauth2CodeRequest,
                                                          HttpServletResponse response) {
        TokenResponse tokenResponse = oauth2Service.login(kakaoOauth2Client.getClient(), oauth2CodeRequest.code(),
            oauth2CodeRequest.redirectUri());
        tokenUtil.setRefreshTokenInCookie(response, tokenResponse.refreshToken());
        return ResponseEntity.ok(new AccessTokenResponse(tokenResponse.memberId(), tokenResponse.accessToken()));
    }
}
