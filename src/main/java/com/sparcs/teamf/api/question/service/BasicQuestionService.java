package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.question.dto.TailQuestionResponse;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import com.sparcs.teamf.domain.question.QuestionService;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicQuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();


    public TailQuestionResponse getRandomQuestion(long categoryId) {
        List<Question> questions = questionRepository.findQuestionByMidCategory_IdAndAnswerIsNotNull(categoryId);
        int questionIndex = random.nextInt(questions.size());
        checkNextQuestionHasAnswer(questions.get(0).getId());
        return new TailQuestionResponse(questions.get(questionIndex).getId(),
                questions.get(questionIndex).getQuestion());
    }

    private void checkNextQuestionHasAnswer(long questionIndex) {
        List<Question> nextQuestions = questionRepository.findQuestionByParentQuestionId(questionIndex);
        Question nextQuestion = nextQuestions.get(0);
        if (nextQuestion.getAnswer() == null) {
            questionService.generateNextQuestion(nextQuestion.getId());
        }
    }
}
