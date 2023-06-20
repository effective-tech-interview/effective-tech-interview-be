package com.sparcs.teamf.page;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.page.exception.PageQuestionNotFoundException;
import com.sparcs.teamf.page.generator.FeedbackGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

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

    public void addMemberAnswer(long pageQuestionId, String memberAnswer) {
        PageQuestion pageQuestion = pageQuestions.stream()
                .filter(pq -> pq.getId() == pageQuestionId)
                .findFirst()
                .orElseThrow(PageQuestionNotFoundException::new);
        pageQuestion.updateFeedback(memberAnswer);
    }

    public void addFeedback(long pageQuestionId, FeedbackGenerator feedbackGenerator) {
        PageQuestion pageQuestion = pageQuestions.stream()
                .filter(pq -> pq.getId() == pageQuestionId)
                .findFirst()
                .orElseThrow(PageQuestionNotFoundException::new);
        pageQuestion.addFeedback(feedbackGenerator);
    }
}
