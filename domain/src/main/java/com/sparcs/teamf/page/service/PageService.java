package com.sparcs.teamf.page.service;

import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.midcategory.MidCategoryRepository;
import com.sparcs.teamf.page.MemberAnswerRepository;
import com.sparcs.teamf.page.PageQuestionRepository;
import com.sparcs.teamf.page.PageRepository;
import com.sparcs.teamf.page.generator.AnswerGenerator;
import com.sparcs.teamf.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class PageService {
    private final AnswerGenerator gptQuestionService;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final PageRepository pageRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final PageQuestionRepository pageQuestionRepository;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final MemberAnswerRepository memberAnswerRepository;


    //    private PageQuestion savedPageBasicQuestionByMidCategory(long midCategoryId, Page page) {
//        //        if (existedPageQuestions.isEmpty()) {
////            PageQuestion savedPageQuestion = savedPageBasicQuestionByMidCategory(midCategoryId, page);
////            return new QuestionsResponse(page.getId(),
////                    page.getMidCategory().getId(),
////                    Collections.singletonList(QuestionResponse.from(savedPageQuestion)));
////        }
//        List<Question> questions = questionRepository.findQuestionByParentQuestionIdIsNullAndMidCategory_Id(
//                midCategoryId);
//        Question basicQuestion = questions.get(random.nextInt(questions.size()));
//        return pageQuestionRepository.save(new PageQuestion(basicQuestion, page));
//    }
//
//    private PageQuestion savedPageTailQuestionByParentQuestion(Page page, Question parentQuestion) {
//        List<Question> questionPage = questionRepository.findQuestionByParentQuestionId(parentQuestion.getId());
//        if (questionPage.isEmpty()) {
//            String nextQuestion = gptQuestionService.generateQuestion(
//                    parentQuestion.getMidCategory().getMainCategory().getName(),
//                    parentQuestion.getMidCategory().getName(),
//                    parentQuestion.getQuestion(),
//                    parentQuestion.getAnswer());
//            Question savedQuestion = questionRepository.save(
//                    new Question(nextQuestion, parentQuestion.getMidCategory()));
//            return pageQuestionRepository.save(new PageQuestion(savedQuestion, page));
//        }
//        Question question = questionPage.get(0);
//        return pageQuestionRepository.save(new PageQuestion(question, page));
//    }
}
