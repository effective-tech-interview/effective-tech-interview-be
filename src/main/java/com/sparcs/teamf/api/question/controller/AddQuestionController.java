package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.api.question.dto.AddQuestionRequest;
import com.sparcs.teamf.api.question.dto.AddQuestionResponse;
import com.sparcs.teamf.api.question.service.AddQuestionService;
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
    public AddQuestionResponse addQuestion(@RequestBody AddQuestionRequest addQuestionRequest) {
        return addQuestionService.addQuestion(addQuestionRequest);
    }
}
