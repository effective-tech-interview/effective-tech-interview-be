package com.sparcs.teamf.question.service;

import com.sparcs.teamf.gpt.Gpt;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.page.Page;
import com.sparcs.teamf.page.PageQuestion;
import com.sparcs.teamf.page.PageQuestionRepository;
import com.sparcs.teamf.page.PageRepository;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import com.sparcs.teamf.question.dto.PageResponse;
import com.sparcs.teamf.question.dto.QuestionResponse;
import com.sparcs.teamf.question.dto.QuestionsResponse;
import com.sparcs.teamf.question.exception.MemberNotFoundException;
import com.sparcs.teamf.question.exception.PageNotFountException;
import com.sparcs.teamf.question.exception.PageOwnerMismatchException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageQuestionService {

    private final Gpt gpt;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final PageRepository pageRepository;
    private final PageQuestionRepository pageQuestionRepository;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Transactional
    public PageResponse getPage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Page savedPage = pageRepository.save(new Page(member));
        return new PageResponse(savedPage.getId());
    }

    @Transactional
    public QuestionsResponse getPageQuestions(Long memberId, long midCategoryId, long pageId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFountException::new);
        validateMember(member, page);

        List<PageQuestion> existedPageQuestions = page.getPageQuestions();
        List<QuestionResponse> questionResponses;
        if (existedPageQuestions.isEmpty()) {
            PageQuestion savedPageQuestion = savedPageBasicQuestionByMidCategory(midCategoryId, page);
            return new QuestionsResponse(page.getId(),
                    Collections.singletonList(QuestionResponse.from(savedPageQuestion)));
        }
        questionResponses = existedPageQuestions.stream().map(QuestionResponse::from).collect(Collectors.toList());
        if (!existedPageQuestions.isEmpty() && existedPageQuestions.size() < 4) {
            Question parentQuestion = existedPageQuestions.get(existedPageQuestions.size() - 1).getQuestion();
            questionResponses.add(QuestionResponse.from(savedPageTailQuestionByParentQuestion(page, parentQuestion)));
        }
        return new QuestionsResponse(page.getId(), questionResponses);
    }

    private void validateMember(Member member, Page page) {
        if (page.getMember() != member) {
            throw new PageOwnerMismatchException();
        }
    }

    private PageQuestion savedPageBasicQuestionByMidCategory(long midCategoryId, Page page) {
        List<Question> questions = questionRepository.findQuestionByParentQuestionIdIsNullAndMidCategory_Id(
                midCategoryId);
        Question basicQuestion = questions.get(random.nextInt(questions.size()));
        return pageQuestionRepository.save(new PageQuestion(basicQuestion, page));
    }

    private PageQuestion savedPageTailQuestionByParentQuestion(Page page, Question parentQuestion) {
        List<Question> questionPage = questionRepository.findQuestionByParentQuestionId(parentQuestion.getId());
        //todo gpt 로 질문 생성 로직 추가
//        Question question = questionPage.isEmpty() ? gpt.loadNextQuestion(parentQuestion) : questionPage.get(0);
        return null;
//        return pageQuestionRepository.save(new PageQuestion(question, page));
    }
}
