package com.sparcs.teamf.api.answer.controller;

import com.sparcs.teamf.answer.dto.FeedbackRequest;
import com.sparcs.teamf.answer.dto.SaveMemberAnswerRequest;
import com.sparcs.teamf.answer.service.PageAnswerService;
import com.sparcs.teamf.dto.EffectiveMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{pageId}/questions/{questionId}")
    @Operation(summary = "멤버 답변 저장 및 업데이트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
        @ApiResponse(responseCode = "401", description = "unauthorized"),
        @ApiResponse(responseCode = "403", description = "forbidden"),
        @ApiResponse(responseCode = "404", description = "not found")})
    public void saveMemberAnswer(@PathVariable("pageId") long pageId,
                                 @PathVariable("questionId") long pageQuestionId,
                                 @RequestBody SaveMemberAnswerRequest request,
                                 @AuthenticationPrincipal EffectiveMember member) {
        pageAnswerService.saveMemberAnswer(member.getMemberId(), pageId, pageQuestionId, request.memberAnswer());
    }

    @PostMapping("/{pageId}/questions/{questionId}/feedback")
    @Operation(summary = "멤버 답변 피드백")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(responseCode = "500", description = "internal server error"),
        @ApiResponse(responseCode = "401", description = "unauthorized"),
        @ApiResponse(responseCode = "403", description = "forbidden"),
        @ApiResponse(responseCode = "404", description = "not found")})
    public ResponseEntity<Void> feedback(@PathVariable long pageId,
                                         @PathVariable long questionId,
                                         @RequestBody FeedbackRequest feedbackRequest) {
        validateRequest(pageId, questionId, feedbackRequest);
        pageAnswerService.feedback(feedbackRequest);
        return ResponseEntity.ok().build();
    }

    private void validateRequest(long pageId, long questionId, FeedbackRequest feedbackRequest) {
        if (pageId != feedbackRequest.pageId()) {
            throw new IllegalArgumentException("pageId is not matched");
        }
        if (questionId != feedbackRequest.questionId()) {
            throw new IllegalArgumentException("questionId is not matched");
        }
    }
}
