package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.api.auth.dto.EffectiveMember;
import com.sparcs.teamf.api.question.dto.QuestionsResponse;
import com.sparcs.teamf.api.question.service.PageQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Question-v2")
@RequestMapping("/v2/pages")
public class PageQuestionController {

    private final PageQuestionService pageQuestionService;

    @GetMapping("/questions")
    @Operation(summary = "질문 리스트 조회")
    public QuestionsResponse getQuestions(@RequestParam(value = "midCategoryId") long midCategoryId,
                                          @AuthenticationPrincipal EffectiveMember member) {
        return pageQuestionService.getPageBasicQuestion(midCategoryId, member.getMemberId());
    }
}
