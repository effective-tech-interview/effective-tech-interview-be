package com.sparcs.teamf.api.answer.service;

import com.sparcs.teamf.api.answer.dto.AnswerResponse;
import com.sparcs.teamf.api.answer.exception.AnswerNotFoundException;
import com.sparcs.teamf.common.util.Repeat;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import com.sparcs.teamf.domain.question.QuestionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    public AnswerResponse getAnswer(long questionId) throws InterruptedException {
        var question = Repeat.repeat(() -> findQuestionById(questionId),
                this::needToRepeat);
        validateQuestion(question, questionId);
        return new AnswerResponse(questionId, question.get().get().getAnswer());
    }

    private void validateQuestion(Optional<Optional<Question>> question, long questionId) {
        if (question.isEmpty() || question.get().isEmpty() || question.get().get().getAnswer() == null) {
//            questionService.generateNextQuestion(questionId);
            throw new AnswerNotFoundException();
        }
    }

    private Optional<Question> findQuestionById(long questionId) {
        return questionRepository.findById(questionId);
    }

    private boolean needToRepeat(Optional<Question> question) {
        if (question.isEmpty()) {
            return true;
        }
        return question.get().getAnswer() == null;
    }
}
