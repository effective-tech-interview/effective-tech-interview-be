package com.sparcs.teamf.question;

import com.sparcs.teamf.BaseEntity;
import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.page.PageQuestion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mid_category_id")
    private MidCategory midCategory;

    @OneToMany(mappedBy = "question")
    private List<PageQuestion> pageQuestions = new ArrayList<>();

    @Column(nullable = false)
    private String question;

    @Column(length = 2000)
    private String answer;

    private Long parentQuestionId;

    public Question(String question, MidCategory midCategory) {
        this.question = question;
        this.midCategory = midCategory;
    }

    public Question(String question, MidCategory midCategory, String answer) {
        this.question = question;
        this.midCategory = midCategory;
        this.answer = answer;
    }

    public void updateParentQuestionId(Long parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
