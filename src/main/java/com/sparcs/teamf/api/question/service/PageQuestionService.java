package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.question.dto.QuestionResponse;
import com.sparcs.teamf.api.question.dto.QuestionsResponse;
import com.sparcs.teamf.api.question.exception.PageNotFountException;
import com.sparcs.teamf.api.question.exception.PageOwnerMismatchException;
import com.sparcs.teamf.domain.gpt.Gpt;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
import com.sparcs.teamf.domain.page.Page;
import com.sparcs.teamf.domain.page.PageQuestion;
import com.sparcs.teamf.domain.page.PageQuestionRepository;
import com.sparcs.teamf.domain.page.PageRepository;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
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
    public QuestionsResponse getPageBasicQuestion(long midCategoryId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        List<Question> questions = questionRepository.findQuestionByParentQuestionIdIsNullAndMidCategory_Id(midCategoryId);
        Question basicQuestion = questions.get(random.nextInt(questions.size()));
        Page savedPage = pageRepository.save(new Page(member));
        PageQuestion savedPageQuestion = pageQuestionRepository.save(new PageQuestion(basicQuestion, savedPage));
        QuestionResponse response = QuestionResponse.from(savedPageQuestion);
        return new QuestionsResponse(savedPage.getId(), List.of(response));
    }

    @Transactional
    public QuestionsResponse getPageTailQuestion(Long memberId, long pageId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFountException::new);
        validateMember(member, page);

        List<PageQuestion> existedPageQuestions = page.getPageQuestions();
        int lastPageQuestionIndex = existedPageQuestions.size() - 1;
        List<QuestionResponse> questionResponses = existedPageQuestions.stream().map(QuestionResponse::from).collect(Collectors.toList());
        if (lastPageQuestionIndex >= 3) {
            return new QuestionsResponse(page.getId(), questionResponses);
        }
        Question parentQuestion = existedPageQuestions.get(lastPageQuestionIndex).getQuestion();
        List<Question> questionPage = questionRepository.findQuestionByParentQuestionId(parentQuestion.getId());
        Question question = questionPage.isEmpty() ? gpt.loadNextQuestion(parentQuestion) : questionPage.get(0);

        PageQuestion savedPageQuestion = pageQuestionRepository.save(new PageQuestion(question, page));
        questionResponses.add(QuestionResponse.from(savedPageQuestion));
        return new QuestionsResponse(page.getId(), questionResponses);
    }

    private void validateMember(Member member, Page page) {
        if (page.getMember() != member) {
            throw new PageOwnerMismatchException();
        }
    }
}
