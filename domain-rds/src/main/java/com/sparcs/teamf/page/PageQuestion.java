package com.sparcs.teamf.page;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.page.exception.AnswerNotFoundException;
import com.sparcs.teamf.page.generator.AnswerGenerator;
import com.sparcs.teamf.page.generator.FeedbackGenerator;
import com.sparcs.teamf.question.Question;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(length = 2000)
    private String aiAnswer;

    public PageQuestion(Question question, Page page) {
        this.question = question;
        this.page = page;
    }

    public void updateFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void addFeedback(FeedbackGenerator feedbackGenerator) {
        if (memberAnswer == null || memberAnswer.getMemberAnswer() == null) {
            throw new AnswerNotFoundException();
        }
        feedback = feedbackGenerator.generateFeedback(question, memberAnswer.getMemberAnswer());
    }

    public void addAiAnswer(AnswerGenerator answerGenerator) {
        if (question.getAnswer() == null) {
            String generatedAnswer = answerGenerator.generateAnswer(question.getMidCategory().getMainCategory().getName(),
                    question.getMidCategory().getName(),
                    question.getQuestion());
            question.updateAnswer(generatedAnswer);
        }
        aiAnswer = question.getAnswer();
    }

    public void updateMemberAnswer(String memberAnswer) {
        if (this.memberAnswer == null) {
            this.memberAnswer = new MemberAnswer(memberAnswer, this);
        } else {
            this.memberAnswer.updateMemberAnswer(memberAnswer);
        }
    }
}
