package com.sparcs.teamf.page.service;

import com.sparcs.teamf.member.MemberRepository;
import com.sparcs.teamf.page.Page;
import com.sparcs.teamf.page.PageQuestion;
import com.sparcs.teamf.page.PageRepository;
import com.sparcs.teamf.page.dto.QuestionResponse;
import com.sparcs.teamf.page.dto.QuestionsResponse;
import com.sparcs.teamf.page.exception.PageNotFountException;
import com.sparcs.teamf.page.exception.PageOwnerMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PageQueryService {
    private final MemberRepository memberRepository;
    private final PageRepository pageRepository;

    public QuestionsResponse getPageQuestions(long memberId, long pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFountException::new);
        validateMember(memberId, page);
        List<PageQuestion> existedPageQuestions = page.getPageQuestions();
        List<QuestionResponse> questionResponses = existedPageQuestions.stream()
                .map(QuestionResponse::from)
                .toList();
        return new QuestionsResponse(page.getId(), page.getMidCategoryId(), questionResponses);
    }

    private void validateMember(long memberId, Page page) {
        if (page.getMemberId() != memberId) {
            throw new PageOwnerMismatchException();
        }
    }
}
