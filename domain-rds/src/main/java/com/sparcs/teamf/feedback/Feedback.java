package com.sparcs.teamf.feedback;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.member.Member;
import com.sparcs.teamf.question.Question;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Feedback(String feedback) {
        this.feedback = feedback;
    }

    public Feedback(String feedback, Member member, Question question) {
        this.feedback = feedback;
        this.member = member;
        this.question = question;
    }
}
