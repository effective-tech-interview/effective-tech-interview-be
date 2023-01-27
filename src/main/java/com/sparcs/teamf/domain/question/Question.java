package com.sparcs.teamf.domain.question;

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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String question;

    public String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_category_id")
    public MidCategory midCategory;

    private Long parentQuestionId;

    public Question(String question, MidCategory midCategory) {
        this.question = question;
        this.midCategory = midCategory;
    }

    public void updateParentQuestionId(Long parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }
}
