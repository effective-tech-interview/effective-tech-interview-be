package com.sparcs.teamf.answer.service;

import com.sparcs.teamf.answer.dto.AnswerResponse;
import com.sparcs.teamf.answer.dto.FeedbackResponse;
import com.sparcs.teamf.answer.exception.QuestionNotFoundException;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.question.service.GptQuestionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final GptQuestionService gptQuestionService;

    public AnswerResponse getAnswer(long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException();
        }
        if (question.get().getAnswer() == null) {
            String answer = gptQuestionService.generateAnswer(
                question.get().getMidCategory().getMainCategory().getName(),
                question.get().getMidCategory().getName(),
                question.get().getQuestion());
            question.get().updateAnswer(answer);
            Question savedQuestion = questionRepository.save(question.get());
            return new AnswerResponse(savedQuestion.getId(), savedQuestion.getAnswer());
        }
        return new AnswerResponse(questionId, question.get().getAnswer());
    }

    public FeedbackResponse feedback(Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException();
        }
        return null;
    }
}
