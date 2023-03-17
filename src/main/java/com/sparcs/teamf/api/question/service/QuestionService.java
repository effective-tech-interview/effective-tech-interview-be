package com.sparcs.teamf.api.question.service;

import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.question.dto.QuestionResponse;
import com.sparcs.teamf.api.question.dto.QuestionsResponse;
import com.sparcs.teamf.domain.gpt.Gpt;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
import com.sparcs.teamf.domain.page.Page;
import com.sparcs.teamf.domain.page.PageQuestion;
import com.sparcs.teamf.domain.page.PageRepository;
import com.sparcs.teamf.domain.question.Question;
import com.sparcs.teamf.domain.question.QuestionRepository;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final PageRepository pageRepository;
    private final Gpt gpt;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final int QUESTION_TOTAL_NUM = 4;

    @Transactional
    public QuestionsResponse getQuestions(long midCategoryId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        List<Question> questions = questionRepository.findQuestionByParentQuestionIdIsNullAndMidCategory_Id(midCategoryId);
        Question basicQuestion = questions.get(random.nextInt(questions.size()));
        Question[] questionGroup = getQuestionGroup(basicQuestion);
        List<QuestionResponse> questionResponses = Arrays.stream(questionGroup)
            .map(QuestionResponse::from)
            .toList();

        Page page = new Page();
        PageQuestion pageQuestion = new PageQuestion(basicQuestion);
        page.addPageQuestion(pageQuestion);
        member.addPage(page);
        Page savedPage = pageRepository.save(page);
        return new QuestionsResponse(savedPage.getId(), questionResponses);
    }

    private Question[] getQuestionGroup(Question basicQuestion) {
        Question[] questionGroup = new Question[QUESTION_TOTAL_NUM];
        List<Question> tailQuestion = questionRepository.findQuestionByParentQuestionId(
            basicQuestion.getId()); // Optional<Question> 반환하도록 리팩토링 필요
        questionGroup[0] = basicQuestion;
        questionGroup[1] = tailQuestion.get(0);
        for (int i = 1; i < 3; i++) {
            Question parentQuestion = questionGroup[i];
            List<Question> questionByParentQuestionId = questionRepository.findQuestionByParentQuestionId(parentQuestion.getId());
            if (questionByParentQuestionId.isEmpty()) {
                // gpt 호출
            }
            questionGroup[i + 1] = questionByParentQuestionId.get(0);
        }
        return questionGroup;
    }
}