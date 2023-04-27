package com.sparcs.teamf.answer.service;

import com.sparcs.teamf.answer.dto.AnswerResponse;
import com.sparcs.teamf.answer.exception.AnswerNotFoundException;
import com.sparcs.teamf.gpt.Gpt;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.repeat.Repeat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final Gpt gpt;

    public AnswerResponse getAnswer(long questionId) throws InterruptedException {
        Optional<Question> question = Repeat.repeat(() -> findQuestionById(questionId),
                this::needToRepeat,
                AnswerNotFoundException::new);
        return new AnswerResponse(questionId, question.get().getAnswer());
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
