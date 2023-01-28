package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.answer.exception.AnswerNotFoundException;
import com.sparcs.teamf.api.question.dto.TailQuestionResponse;
import com.sparcs.teamf.common.util.Repeat;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TailQuestionService {

    private final QuestionRepository questionRepository;

    public TailQuestionResponse getTailQuestion(long questionId) throws InterruptedException {
        List<Question> question = Repeat.repeat(() -> findQuestionById(questionId),
                this::needToRepeat,
                AnswerNotFoundException::new);
        if (question.isEmpty()) {
            //존재할 수는 없는 케이스. 컴파일러를 위한 코드
            throw new AnswerNotFoundException();
        }
        return new TailQuestionResponse(question.get(0).getId(), question.get(0).getQuestion());
    }

    private List<Question> findQuestionById(long questionId) {
        return questionRepository.findQuestionByParentQuestionId(questionId);
    }

    private boolean needToRepeat(List<Question> question) {
        if (question.isEmpty()) {
            return true;
        }
        return question.get(0).getAnswer() == null;
    }
}
