package com.sparcs.teamf.api.member.controller;

import com.sparcs.teamf.api.auth.dto.ResetPasswordEmailRequest;
import com.sparcs.teamf.api.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Members")
@RestController
@RequestMapping("v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/password-reset")
    @Operation(summary = "비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "internal server error"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "401", description = "invalid one time token"),
            @ApiResponse(responseCode = "403", description = "forbidden"),
            @ApiResponse(responseCode = "404", description = "not found")})
    public void resetPassword(@RequestBody @Valid ResetPasswordEmailRequest request) {
        memberService.resetPassword(request.email(), request.password(), request.confirmPassword());
    }
}
