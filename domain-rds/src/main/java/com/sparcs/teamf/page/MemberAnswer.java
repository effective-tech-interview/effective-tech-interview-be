package com.sparcs.teamf.page;

import com.sparcs.teamf.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_question_id")
    private PageQuestion pageQuestion;

    private String memberAnswer;

    public MemberAnswer(String memberAnswer, PageQuestion pageQuestion) {
        this.memberAnswer = memberAnswer;
        this.pageQuestion = pageQuestion;
    }

    public void updateMemberAnswer(String memberAnswer) {
        this.memberAnswer = memberAnswer;
    }
}
