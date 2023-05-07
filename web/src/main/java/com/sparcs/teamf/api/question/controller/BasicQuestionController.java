package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.question.dto.TailQuestionResponse;
import com.sparcs.teamf.question.service.BasicQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/questions")
@RequiredArgsConstructor
@Tag(name = "Question")
public class BasicQuestionController {

    private final BasicQuestionService basicQuestionService;

    @GetMapping
    @Operation(summary = "기본 질문 랜덤 조회")
    public TailQuestionResponse getQuestions(@RequestParam(value = "midCategoryId") long midCategoryId) {
        return basicQuestionService.getRandomQuestion(midCategoryId);
    }
}
