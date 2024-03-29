package com.sparcs.teamf.answer.service;

import com.sparcs.teamf.answer.exception.PageQuestionNotFoundException;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.page.MemberAnswer;
import com.sparcs.teamf.page.MemberAnswerRepository;
import com.sparcs.teamf.page.Page;
import com.sparcs.teamf.page.PageQuestion;
import com.sparcs.teamf.page.PageQuestionRepository;
import com.sparcs.teamf.page.PageRepository;
import com.sparcs.teamf.question.exception.PageNotFountException;
import com.sparcs.teamf.question.exception.PageOwnerMismatchException;
import com.sparcs.teamf.question.exception.PageQuestionMismatchException;
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
        Member member = memberRepository.findById(memberId).orElseThrow();
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
            throw new PageQuestionMismatchException();
        }
    }
}
