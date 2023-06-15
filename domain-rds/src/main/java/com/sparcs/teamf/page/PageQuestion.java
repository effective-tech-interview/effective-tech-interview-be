package com.sparcs.teamf.page;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.question.Question;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @OneToOne(mappedBy = "pageQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberAnswer memberAnswer;

    @Column(length = 2000)
    private String feedback;

    public PageQuestion(Question question, Page page) {
        this.question = question;
        this.page = page;
    }

    public void updateFeedback(String feedback) {
        this.feedback = feedback;
    }
}
