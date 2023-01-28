package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.question.dto.AddQuestionRequest;
import com.sparcs.teamf.api.question.dto.AddQuestionResponse;
import com.sparcs.teamf.api.question.exception.IllegalMidCategoryException;
import com.sparcs.teamf.domain.gpt.Gpt;
import com.sparcs.teamf.domain.midcategory.MidCategory;
import com.sparcs.teamf.domain.midcategory.MidCategoryRepository;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
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
        gpt.loadBaseQuestions(newQuestion);
        return new AddQuestionResponse(question);
    }
}
