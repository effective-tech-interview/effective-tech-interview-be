package com.sparcs.teamf.page.service;

import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.midcategory.MidCategoryRepository;
import com.sparcs.teamf.midcategory.exception.MidCategoryNotFoundException;
import com.sparcs.teamf.page.Page;
import com.sparcs.teamf.page.PageQuestion;
import com.sparcs.teamf.page.PageRepository;
import com.sparcs.teamf.page.dto.CreatePageRequest;
import com.sparcs.teamf.page.dto.FeedbackRequest;
import com.sparcs.teamf.page.dto.PageResponse;
import com.sparcs.teamf.page.exception.MemberNotFoundException;
import com.sparcs.teamf.page.exception.PageNotFountException;
import com.sparcs.teamf.page.exception.PageOwnerMismatchException;
import com.sparcs.teamf.question.Question;
import com.sparcs.teamf.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Transactional
public class PageCommandService {

    private final MemberRepository memberRepository;
    private final MidCategoryRepository midCategoryRepository;
    private final PageRepository pageRepository;
    private final QuestionRepository questionRepository;
    private final GptQuestionService gptQuestionService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public PageResponse createPage(long memberId, CreatePageRequest createPageRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        MidCategory midCategory = midCategoryRepository.findById(createPageRequest.midCategoryId())
                .orElseThrow(MidCategoryNotFoundException::new);
        Page page = new Page(member, midCategory);
        Page savedPage = pageRepository.save(page);
        createInitialPageQuestion(savedPage);
        return new PageResponse(savedPage.getId());
    }

    private void createInitialPageQuestion(Page savedPage) {
        List<Question> basicQuestion = questionRepository.findQuestionByParentQuestionIdIsNullAndMidCategory_Id(
                savedPage.getMidCategory().getId());
        Question question = basicQuestion.get(random.nextInt(basicQuestion.size()));
        PageQuestion pageQuestion = new PageQuestion(question, savedPage);
        savedPage.addPageQuestion(pageQuestion);
    }

    public void saveMemberAnswer(long memberId, long pageId, long pageQuestionId, String memberAnswer) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFountException::new);
        validateMember(memberId, page);
        page.addMemberAnswer(pageQuestionId, memberAnswer);
    }

    private void validateMember(long memberId, Page page) {
        if (page.getMemberId() != memberId) {
            throw new PageOwnerMismatchException();
        }
    }

    public void feedback(FeedbackRequest feedbackRequest) {
        Page page = pageRepository.findById(feedbackRequest.pageId())
                .orElseThrow(PageNotFountException::new);
        page.addFeedback(feedbackRequest.pageQuestionId(), gptQuestionService);
    }
}
