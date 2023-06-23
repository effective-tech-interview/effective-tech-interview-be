package com.sparcs.teamf.page;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.page.exception.PageQuestionNotFoundException;
import com.sparcs.teamf.page.generator.AnswerGenerator;
import com.sparcs.teamf.page.generator.FeedbackGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PageQuestion> pageQuestions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @OneToOne
    private MidCategory midCategory;

    @Column(name = "mid_category_id", insertable = false, updatable = false)
    private Long midCategoryId;

    public Page(Member member) {
        this.member = member;
    }

    public Page(Member member, MidCategory midCategory) {
        this.member = member;
        this.midCategory = midCategory;
    }

    public void addPageQuestion(PageQuestion pageQuestion) {
        pageQuestions.add(pageQuestion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Page page = (Page) o;
        return Objects.equals(id, page.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addMemberAnswer(long pageQuestionId, String memberAnswer) {
        PageQuestion pageQuestion = pageQuestions.stream()
                .filter(pq -> pq.getId() == pageQuestionId)
                .findFirst()
                .orElseThrow(PageQuestionNotFoundException::new);
        pageQuestion.updateMemberAnswer(memberAnswer);
    }

    public void addFeedback(long pageQuestionId, FeedbackGenerator feedbackGenerator) {
        PageQuestion pageQuestion = pageQuestions.stream()
                .filter(pq -> pq.getId() == pageQuestionId)
                .findFirst()
                .orElseThrow(PageQuestionNotFoundException::new);
        pageQuestion.addFeedback(feedbackGenerator);
    }

    public void addAiAnswer(long pageQuestionId, AnswerGenerator answerGenerator) {
        PageQuestion pageQuestion = pageQuestions.stream()
                .filter(pq -> pq.getId() == pageQuestionId)
                .findFirst()
                .orElseThrow(PageQuestionNotFoundException::new);
        pageQuestion.addAiAnswer(answerGenerator);
    }

    public PageQuestion getQuestionByPageQuestionId(long pageQuestionId) {
        return pageQuestions.stream()
                .filter(pq -> pq.getId() == pageQuestionId)
                .findFirst()
                .orElseThrow(PageQuestionNotFoundException::new);
    }

    public PageQuestion getLastQuestion() {
        return pageQuestions.get(pageQuestions.size() - 1);
    }
}
