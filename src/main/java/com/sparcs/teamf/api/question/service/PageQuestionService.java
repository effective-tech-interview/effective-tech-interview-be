package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.question.dto.QuestionResponse;
import com.sparcs.teamf.api.question.dto.QuestionsResponse;
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
    private static final int QUESTION_TOTAL_NUM = 4;

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

    private Question[] getQuestionGroup(Question basicQuestion) {
        Question[] questionGroup = new Question[QUESTION_TOTAL_NUM];
        questionGroup[0] = basicQuestion;
        for (int i = 0; i < QUESTION_TOTAL_NUM - 1; i++) {
            Question parentQuestion = questionGroup[i];
            List<Question> questionByParentQuestionId = questionRepository.findQuestionByParentQuestionId(parentQuestion.getId());
            if (questionByParentQuestionId.isEmpty()) {
                questionGroup[i + 1] = gpt.loadNextQuestion(parentQuestion);
                return questionGroup;
            }
            questionGroup[i + 1] = questionByParentQuestionId.get(0);
        }
        return questionGroup;
    }
}
