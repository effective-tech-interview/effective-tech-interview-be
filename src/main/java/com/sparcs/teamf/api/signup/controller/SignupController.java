package com.sparcs.teamf.api.signup.controller;

import com.sparcs.teamf.api.auth.dto.TokenResponse;
import com.sparcs.teamf.api.signup.dto.SignupRequest;
import com.sparcs.teamf.api.signup.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Signup")
@RestController
@RequestMapping("v1/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping
    @Operation(summary = "회원 가입")
    public TokenResponse signup(@RequestBody @Valid SignupRequest request) {
        return signupService.signup(request.email(), request.password(), request.confirmPassword());
    }
}
