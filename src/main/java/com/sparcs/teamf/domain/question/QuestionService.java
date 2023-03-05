package com.sparcs.teamf.domain.question;

import com.sparcs.teamf.domain.gpt.Gpt;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    public static final int MAXIMUM_RETRY_COUNT = 3;
    private static final String ANSWER_FORMAT = "%s 카테고리에 대한 질문 %s의 답을 15 단어 내외로 알려 주세요.";
    private static final String NEXT_QUESTION_FORMAT =
            "%s 카테고리 질문 %s 에 대해서 %s 라는 답과 관련된 질문을 10 단어 내외로 하나 알려주세요";
    private static final String GPT_GENERATION_FAILED =
            "Failed to generate next question for question id : %d retry count : %d";
    private static final Logger logger = Logger.getLogger(QuestionService.class.getName());

    private final QuestionRepository questionRepository;
    private final Gpt gpt;

    @Async
    public void generateNextQuestion(long questionId) {
        int count = 0;
        while (++count < MAXIMUM_RETRY_COUNT) {
            try {
                generateQuestion(questionId);
                return;
            } catch (Exception e) {
                logger.warning(String.format(GPT_GENERATION_FAILED, questionId, count));
            }
        }
    }

    private void generateQuestion(long questionId) {
        Question question = loadQuestion(questionId);
        if (question.getAnswer() != null) {
            return;
        }
        var answer = generateAnswer(question);
        var nextQuestion = nextQuestion(question, answer);
        saveQuestion(question, answer, nextQuestion);
    }

    //이 transactional 은 미리 조회를 해두기 위함입니다. todo fetchjoin 을 통해서 미리 가져오는 방식
    @Transactional
    Question loadQuestion(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(IllegalArgumentException::new);
        question.getMidCategory();
        return question;
    }

    @Transactional
    void saveQuestion(Question question, String answer, String nextQuestion) {
        question.updateAnswer(answer);
        questionRepository.save(question);
        questionRepository.save(new Question(nextQuestion, question.getMidCategory(), question.getId()));
    }

    private String nextQuestion(Question question, String generatedAnswer) {
        String midCategoryName = question.getMidCategory().getName();
        return gpt.ask(String.format(
                NEXT_QUESTION_FORMAT,
                midCategoryName,
                question.getQuestion(),
                generatedAnswer));
    }

    private String generateAnswer(Question question) {
        String midCategoryName = question.getMidCategory().getName();
        return gpt.ask(String.format(ANSWER_FORMAT,
                midCategoryName, question.getQuestion()));
    }
}
