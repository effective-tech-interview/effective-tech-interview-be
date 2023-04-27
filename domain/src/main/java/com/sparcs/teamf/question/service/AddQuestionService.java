package com.sparcs.teamf.question.service;

import com.sparcs.teamf.gpt.Gpt;
import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.midcategory.MidCategoryRepository;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.question.dto.AddQuestionRequest;
import com.sparcs.teamf.question.dto.AddQuestionResponse;
import com.sparcs.teamf.question.exception.IllegalMidCategoryException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddQuestionService {

    private final QuestionRepository questionRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final Gpt gpt;

    @Transactional
    public AddQuestionResponse addQuestion(AddQuestionRequest addQuestionRequest) {
        String question = addQuestionRequest.question();
        Long midCategoryId = addQuestionRequest.midCategoryId();
        if (midCategoryId == null) {
            throw new IllegalMidCategoryException();
        }
        MidCategory midCategory = midCategoryRepository.findById(midCategoryId)
                .orElseThrow(IllegalMidCategoryException::new);
        Question newQuestion = questionRepository.save(new Question(question, midCategory));
        //todo gpt 관련 로직 추가
//        gpt.loadBaseQuestions(newQuestion);
//        return new AddQuestionResponse(question);
        return new AddQuestionResponse("question");
    }
}
