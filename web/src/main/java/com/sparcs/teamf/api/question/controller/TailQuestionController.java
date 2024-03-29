package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.question.dto.TailQuestionResponse;
import com.sparcs.teamf.question.service.TailQuestionService;
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
@Tag(name = "Question")
public class TailQuestionController {

    private final TailQuestionService tailQuestionService;

    @GetMapping("/{questionId}/tail")
    @Operation(summary = "꼬리 질문 조회")
    public TailQuestionResponse getTailQuestion(@PathVariable("questionId") long questionId)
            throws InterruptedException {
        return tailQuestionService.getTailQuestion(questionId);
    }
}
