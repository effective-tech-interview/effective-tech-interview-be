package com.sparcs.teamf.api.answer.service;

import com.sparcs.teamf.api.answer.dto.AnswerResponse;
import com.sparcs.teamf.api.answer.exception.AnswerNotFoundException;
import com.sparcs.teamf.api.answer.exception.QuestionNotFoundException;
import com.sparcs.teamf.common.util.Repeat;
import com.sparcs.teamf.domain.gpt.Gpt;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final Gpt gpt;

    public AnswerResponse getAnswer(long questionId) throws InterruptedException {
        Optional<Question> target = findQuestionById(questionId);
        if (target.isEmpty()) {
            throw new QuestionNotFoundException();
        }
        gpt.loadNextQuestion(target.get());

        Optional<Question> question = Repeat.repeat(() -> findQuestionById(questionId),
                this::needToRepeat,
                AnswerNotFoundException::new);
        if (question.isEmpty()) {
            //존재할 수는 없는 케이스. 컴파일러를 위한 코드
            throw new AnswerNotFoundException();
        }
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
