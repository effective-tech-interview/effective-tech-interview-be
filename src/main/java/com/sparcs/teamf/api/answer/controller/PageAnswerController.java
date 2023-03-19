package com.sparcs.teamf.api.answer.controller;

import com.sparcs.teamf.api.answer.dto.SaveMemberAnswerRequest;
import com.sparcs.teamf.api.answer.service.PageAnswerService;
import com.sparcs.teamf.api.auth.dto.EffectiveMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v2/pages")
@RequiredArgsConstructor
@Tag(name = "Answer-v2")
public class PageAnswerController {

    private final PageAnswerService pageAnswerService;

    @PostMapping("/{pageId}/questions/{pageQuestionId}")
    public void saveMemberAnswer(@PathVariable("pageId") long pageId,
                                 @PathVariable("pageQuestionId") long pageQuestionId,
                                 @RequestBody SaveMemberAnswerRequest request,
                                 @AuthenticationPrincipal EffectiveMember member) {
        pageAnswerService.saveMemberAnswer(member.getMemberId(), pageId, pageQuestionId, request.memberAnswer());
    }
}
