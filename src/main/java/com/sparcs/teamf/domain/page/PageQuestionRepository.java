package com.sparcs.teamf.domain.page;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PageQuestionRepository extends CrudRepository<PageQuestion, Long> {
    List<PageQuestion> findAllByPage(Page page);
}
