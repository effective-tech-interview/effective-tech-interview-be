package com.sparcs.teamf.api.answer.service;

import com.sparcs.teamf.api.answer.exception.PageQuestionNotFoundException;
import com.sparcs.teamf.api.member.exception.MemberNotFoundException;
import com.sparcs.teamf.api.question.exception.PageNotFountException;
import com.sparcs.teamf.api.question.exception.PageOwnerMismatchException;
import com.sparcs.teamf.domain.member.Member;
import com.sparcs.teamf.domain.member.MemberRepository;
import com.sparcs.teamf.domain.page.MemberAnswer;
import com.sparcs.teamf.domain.page.MemberAnswerRepository;
import com.sparcs.teamf.domain.page.Page;
import com.sparcs.teamf.domain.page.PageQuestion;
import com.sparcs.teamf.domain.page.PageQuestionRepository;
import com.sparcs.teamf.domain.page.PageRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageAnswerService {

    private final PageRepository pageRepository;
    private final MemberRepository memberRepository;
    private final PageQuestionRepository pageQuestionRepository;
    private final MemberAnswerRepository memberAnswerRepository;

    @Transactional
    public void saveMemberAnswer(long memberId, long pageId, long pageQuestionId, String memberAnswer) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFountException::new);
        PageQuestion pageQuestion = pageQuestionRepository.findById(pageQuestionId)
                .orElseThrow(PageQuestionNotFoundException::new);
        validatePageQuestion(member, page, pageQuestion);

        if (pageQuestion.getMemberAnswer() != null) {
            pageQuestion.getMemberAnswer().updateMemberAnswer(memberAnswer);
            return;
        }
        MemberAnswer answer = new MemberAnswer(memberAnswer, pageQuestion);
        memberAnswerRepository.save(answer);
    }

    private void validatePageQuestion(Member member, Page page, PageQuestion pageQuestion) {
        if (page.getMember() != member) {
            throw new PageOwnerMismatchException();
        }
        if (pageQuestion.getPage() != page) {
            throw new PageQuestionNotFoundException();
        }
    }
}
