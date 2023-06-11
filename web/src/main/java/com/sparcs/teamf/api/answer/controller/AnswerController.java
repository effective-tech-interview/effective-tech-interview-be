package com.sparcs.teamf.api.answer.controller;

import com.sparcs.teamf.answer.dto.AnswerResponse;
import com.sparcs.teamf.answer.dto.FeedbackRequest;
import com.sparcs.teamf.answer.dto.FeedbackResponse;
import com.sparcs.teamf.answer.service.AnswerService;
import com.sparcs.teamf.dto.EffectiveMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/questions")
@RequiredArgsConstructor
@Tag(name = "Answer")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("{questionId}/answer")
    @Operation(summary = "질문에 대한 답변 조회")
    public AnswerResponse getAnswers(@PathVariable("questionId") String questionId) {
        return answerService.getAnswer(Long.parseLong(questionId));
    }

    @PostMapping("{questionId}/feedback")
    @Operation(summary = "질문에 대한 답변 피드백")
    public ResponseEntity<FeedbackResponse> feedback(@PathVariable("questionId") Long questionId,
                                                     @RequestBody @Valid FeedbackRequest feedbackRequest,
                                                     @AuthenticationPrincipal EffectiveMember member) {
        validateRequest(questionId, feedbackRequest);
        FeedbackResponse feedback1 = answerService.feedback(questionId, feedbackRequest, member.getMemberId());
        return ResponseEntity.ok(feedback1);
    }

    private void validateRequest(Long questionId, FeedbackRequest feedbackRequest) {
        if (!Objects.equals(questionId, feedbackRequest.questionId())) {
            throw new IllegalArgumentException("questionId가 일치하지 않습니다.");
        }
    }
}
