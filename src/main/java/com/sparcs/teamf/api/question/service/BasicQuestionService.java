package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.question.dto.TailQuestionResponse;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicQuestionService {

    private final QuestionRepository questionRepository;

    public TailQuestionResponse getRandomQuestion(long categoryId) {
        List<Question> questions = questionRepository.findQuestionByParentQuestionIdIsNullAndMidCategory_Id(categoryId);
        int questionIndex = ThreadLocalRandom.current().nextInt(questions.size());
        return new TailQuestionResponse(questions.get(questionIndex).getId(),
                questions.get(questionIndex).getQuestion());
    }
}
