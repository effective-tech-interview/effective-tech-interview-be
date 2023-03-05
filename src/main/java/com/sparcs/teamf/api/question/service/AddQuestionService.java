package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.question.dto.AddQuestionRequest;
import com.sparcs.teamf.api.question.dto.AddQuestionResponse;
import com.sparcs.teamf.api.question.exception.IllegalMidCategoryException;
import com.sparcs.teamf.domain.midcategory.MidCategory;
import com.sparcs.teamf.domain.midcategory.MidCategoryRepository;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import com.sparcs.teamf.domain.question.QuestionService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddQuestionService {

    private final QuestionRepository questionRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final QuestionService questionService;

    @Transactional
    public AddQuestionResponse addQuestion(AddQuestionRequest addQuestionRequest) {
        MidCategory midCategory = validateMidCategoryId(addQuestionRequest.midCategoryId());
        String question = addQuestionRequest.question();
        Question newQuestion = questionRepository.save(new Question(question, midCategory));

        questionService.generateNextQuestion(newQuestion.getId());
        return new AddQuestionResponse(question);
    }

    private MidCategory validateMidCategoryId(Long midCategoryId) {
        if (midCategoryId == null) {
            throw new IllegalMidCategoryException();
        }
        return midCategoryRepository.findById(midCategoryId)
                .orElseThrow(IllegalMidCategoryException::new);
    }
}
