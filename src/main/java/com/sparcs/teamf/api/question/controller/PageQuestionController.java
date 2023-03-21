package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.api.auth.dto.EffectiveMember;
import com.sparcs.teamf.api.question.dto.PageBasicQuestionResponse;
import com.sparcs.teamf.api.question.dto.QuestionsResponse;
import com.sparcs.teamf.api.question.service.PageQuestionService;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(summary = "기본 질문 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionsResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public PageBasicQuestionResponse getPageBasicQuestions(@AuthenticationPrincipal EffectiveMember member) {
        return pageQuestionService.getPageBasicQuestion(member.getMemberId());
    }

    @GetMapping("/{pageId}/questions")
    @Operation(summary = "기본, 꼬리 질문 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionsResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public QuestionsResponse getPageTailQuestion(@PathVariable("pageId") long pageId,
                                                 @RequestParam(value = "midCategoryId") long midCategoryId,
                                                 @AuthenticationPrincipal EffectiveMember member) {
        return pageQuestionService.getPageTailQuestion(member.getMemberId(), midCategoryId, pageId);
    }
}
