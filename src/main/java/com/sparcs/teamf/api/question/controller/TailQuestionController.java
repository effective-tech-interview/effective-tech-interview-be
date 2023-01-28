package com.sparcs.teamf.api.question.controller;

import com.sparcs.teamf.api.question.dto.TailQuestionResponse;
import com.sparcs.teamf.api.question.service.TailQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/questions")
@RequiredArgsConstructor
public class TailQuestionController {

    private final TailQuestionService tailQuestionService;

    @GetMapping("/{questionId}/tail")
    public TailQuestionResponse getTailQuestion(@PathVariable("questionId") long questionId)
            throws InterruptedException {
        return tailQuestionService.getTailQuestion(questionId);
    }
}
