package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.question.dto.AddQuestionRequest;
import com.sparcs.teamf.question.dto.AddQuestionResponse;
import com.sparcs.teamf.question.service.AddQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Question")
@RequestMapping("/v1/questions")
public class AddQuestionController {

    private final AddQuestionService addQuestionService;

    @PostMapping
    @Operation(summary = "기본 질문, 꼬리 질문 생성 및 저장")
    public AddQuestionResponse addQuestion(@RequestBody AddQuestionRequest addQuestionRequest) {
        return addQuestionService.addQuestion(addQuestionRequest);
    }
}
