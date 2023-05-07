package com.sparcs.teamf.question.service;

import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.midcategory.MidCategoryRepository;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.question.dto.AddQuestionResponse;
import com.sparcs.teamf.question.exception.IllegalMidCategoryException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddQuestionService {

    private final QuestionRepository questionRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final GptQuestionService gptQuestionService;

    public AddQuestionResponse addQuestion(Long midCategoryId, String question) {
        if (midCategoryId == null) {
            throw new IllegalMidCategoryException();
        }
        MidCategory midCategory = midCategoryRepository.findById(midCategoryId)
                .orElseThrow(IllegalMidCategoryException::new);
        List<Question> newQuestion = generateFourQuestions(midCategory, question);
        questionRepository.saveAll(newQuestion);
        return new AddQuestionResponse(newQuestion.get(0).getQuestion());
    }

    private List<Question> generateFourQuestions(MidCategory midCategory, String question) {
        List<Question> questions = new ArrayList<>();
        String midCategoryName = midCategory.getName();
        String mainCategoryName = midCategory.getMainCategory().getName();
        String answer = gptQuestionService.generateAnswer(mainCategoryName, midCategoryName, question);
        questions.add(new Question(question, midCategory, answer));
        String question1 = gptQuestionService.generateQuestion(mainCategoryName, midCategoryName, question, answer);
        String answer1 = gptQuestionService.generateAnswer(mainCategoryName, midCategoryName, question1);
        questions.add(new Question(question1, midCategory, answer1));
        String question2 = gptQuestionService.generateQuestion(mainCategoryName, midCategoryName, question, answer1);
        String answer2 = gptQuestionService.generateAnswer(mainCategoryName, midCategoryName, question2);
        questions.add(new Question(question2, midCategory, answer2));
        String question3 = gptQuestionService.generateQuestion(mainCategoryName, midCategoryName, question, answer2);
        String answer3 = gptQuestionService.generateAnswer(mainCategoryName, midCategoryName, question3);
        questions.add(new Question(question3, midCategory, answer3));
        String question4 = gptQuestionService.generateQuestion(mainCategoryName, midCategoryName, question, answer3);
        questions.add(new Question(question4, midCategory));

        return questions;
    }
}
