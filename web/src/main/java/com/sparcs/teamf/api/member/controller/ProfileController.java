package com.sparcs.teamf.api.member.controller;

import com.sparcs.teamf.dto.EffectiveMember;
import com.sparcs.teamf.dto.MemberProfileResponse;
import com.sparcs.teamf.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Profile")
@RestController
@RequestMapping("v1/members")
@RequiredArgsConstructor
public class ProfileController {

    private final MemberService memberService;

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
}
