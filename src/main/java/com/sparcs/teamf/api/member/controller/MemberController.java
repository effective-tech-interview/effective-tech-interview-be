package com.sparcs.teamf.api.member.controller;

import com.sparcs.teamf.api.emailauth.dto.SendEmailRequest;
import com.sparcs.teamf.api.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member")
@RestController
@RequestMapping("v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/password-reset/email")
    @Operation(summary = "비밀번호 재설정 인증 코드 전송")
    public void sendPasswordResetCode(@RequestBody @Valid SendEmailRequest request) {
        memberService.sendPasswordResetCode(request.email());
    }
}
