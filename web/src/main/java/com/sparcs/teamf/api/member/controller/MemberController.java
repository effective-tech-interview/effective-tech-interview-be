package com.sparcs.teamf.api.member.controller;

import com.sparcs.teamf.api.auth.dto.ResetPasswordEmailRequest;
import com.sparcs.teamf.dto.EffectiveMember;
import com.sparcs.teamf.dto.MemberProfileResponse;
import com.sparcs.teamf.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @SecurityRequirements
    public void resetPassword(@RequestBody @Valid ResetPasswordEmailRequest request) {
        memberService.resetPassword(request.email(), request.password(), request.confirmPassword());
    }

    @GetMapping("/profile")
    @Operation(summary = "멤버 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemberProfileResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public MemberProfileResponse getMemberProfile(@AuthenticationPrincipal EffectiveMember member) {
        return memberService.getMemberProfile(member.getMemberId());
    }

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
