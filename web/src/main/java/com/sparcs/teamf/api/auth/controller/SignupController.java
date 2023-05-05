package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.dto.SignupRequest;
import com.sparcs.teamf.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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
@SecurityRequirements
public class SignupController {

    private final SignupService signupService;

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
}
