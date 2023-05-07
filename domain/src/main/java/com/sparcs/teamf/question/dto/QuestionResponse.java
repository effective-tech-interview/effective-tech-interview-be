package com.sparcs.teamf.question.dto;

import com.sparcs.teamf.page.MemberAnswer;
import com.sparcs.teamf.page.PageQuestion;

public record QuestionResponse(Long pageQuestionId,
                               String question,
                               String userAnswer,
                               String aiAnswer) {

    public static QuestionResponse from(PageQuestion pageQuestion) {
        MemberAnswer memberAnswer = pageQuestion.getMemberAnswer();
        String memberAnswerResponse = (memberAnswer != null) ? memberAnswer.getMemberAnswer() : "";
        return new QuestionResponse(pageQuestion.getId(), pageQuestion.getQuestion().getQuestion(),
                memberAnswerResponse,
                pageQuestion.getQuestion().getAnswer());
    }
}
