package com.sparcs.teamf.question.service;

import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.question.dto.TailQuestionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TailQuestionService {

    private final QuestionRepository questionRepository;

    public TailQuestionResponse getTailQuestion(long questionId) throws InterruptedException {
        List<Question> question = questionRepository.findQuestionByParentQuestionId(questionId);
        if (needToRepeat(question)) {

        }
//        List<Question> question = Repeat.repeat(() -> findQuestionById(questionId),
//                this::needToRepeat,
//                AnswerNotFoundException::new);
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
