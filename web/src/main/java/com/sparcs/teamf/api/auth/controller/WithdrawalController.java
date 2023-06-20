package com.sparcs.teamf.api.auth.controller;

import com.sparcs.teamf.dto.EffectiveMember;
import com.sparcs.teamf.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Withdrawal")
@RestController
@RequestMapping("v1/members")
public class WithdrawalController {

    private final MemberService memberService;

    @DeleteMapping
    @Operation(summary = "멤버 탈퇴")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
        @ApiResponse(responseCode = "401", description = "unauthorized"),
        @ApiResponse(responseCode = "404", description = "not found")})
    public void delete(@AuthenticationPrincipal EffectiveMember member) {
        memberService.delete(member.getMemberId());
    }
}
