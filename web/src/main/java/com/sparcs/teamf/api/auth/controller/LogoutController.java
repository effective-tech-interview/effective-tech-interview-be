package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Logout")
@RequestMapping("v1/auth")
@SecurityRequirements
public class LogoutController {

    private final AuthService authService;


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
}
