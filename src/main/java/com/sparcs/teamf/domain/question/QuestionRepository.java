package com.sparcs.teamf.domain.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findQuestionByParentQuestionId(long parentQuestionId);

    List<Question> findQuestionByParentQuestionIdIsNullAndMidCategory_Id(long categoryId);
}
