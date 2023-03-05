package com.sparcs.teamf.domain.question;

import com.sparcs.teamf.domain.BaseEntity;
import com.sparcs.teamf.domain.midcategory.MidCategory;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_category_id")
    private MidCategory midCategory;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(length = 2000)
    private String answer;

    private Long parentQuestionId;

    public Question(String question, MidCategory midCategory) {
        this.question = question;
        this.midCategory = midCategory;
    }

    public Question(String question, MidCategory midCategory, long parentQuestionId) {
        this.question = question;
        this.midCategory = midCategory;
        this.parentQuestionId = parentQuestionId;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
