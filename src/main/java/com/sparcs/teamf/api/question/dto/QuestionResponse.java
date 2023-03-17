package com.sparcs.teamf.api.question.dto;

import com.sparcs.teamf.domain.question.Question;

public record QuestionResponse(Long questionId,
                               String question,
                               String userAnswer,
                               String aiAnswer) {

    public static QuestionResponse from(Question question) {
        return new QuestionResponse(question.getId(), question.getQuestion(), "", question.getAnswer());
    }
}
