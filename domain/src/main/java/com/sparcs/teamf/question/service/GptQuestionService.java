package com.sparcs.teamf.question.service;

import com.sparcs.teamf.gpt.Gpt;
import com.sparcs.teamf.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptQuestionService {

    private static final String ANSWER_FORMAT = "%s %s 카테고리에 대한 질문 %s의 답을 알려 주세요.";
    private static final String NEXT_QUESTION_FORMAT =
        "%s %s 카테고리에 에 대한 질문 %s 에 %s 라는 답을 했을 때 생길 수 있는 다음 질문을 하나 알려주세요";
    private static final String FEEDBACK_FORMAT = "%s 카테고리에 질문 %s 에 %s 라는 답을 했을 때, 이 답변이 적절한지 평가해주세요.";

    private final Gpt gpt;

    public String generateAnswer(String mainCategoryName, String midCategoryName, String question) {
        return gpt.ask(generatePromptForAnswer(mainCategoryName, midCategoryName, question));
    }

    private String generatePromptForAnswer(String mainCategoryName, String midCategoryName, String question) {
        return String.format(ANSWER_FORMAT, mainCategoryName, midCategoryName, question);
    }

    public String generateQuestion(String mainCategoryName, String midCategoryName, String question,
                                   String answer) {
        return gpt.ask(generatePromptForNextQuestion(mainCategoryName, midCategoryName, question, answer));
    }

    private String generatePromptForNextQuestion(String mainCategoryName, String midCategoryName, String question,
                                                 String answer) {
        return String.format(NEXT_QUESTION_FORMAT, mainCategoryName, midCategoryName, question, answer);
    }

    public String generateFeedback(Question question, String answer) {
        String feedbackRequest = generatePromptForFeedback(question, answer);
        return gpt.ask(feedbackRequest);
    }

    private String generatePromptForFeedback(Question question, String answer) {
        return String.format(FEEDBACK_FORMAT, question.getMidCategory().getName(),
            question.getQuestion(), answer);
    }
}
