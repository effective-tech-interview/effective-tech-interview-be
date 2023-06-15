package com.sparcs.teamf.api.answer.controller;

import com.sparcs.teamf.answer.dto.AnswerResponse;
import com.sparcs.teamf.answer.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
