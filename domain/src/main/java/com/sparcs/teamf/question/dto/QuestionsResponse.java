package com.sparcs.teamf.question.dto;

import java.util.List;

public record QuestionsResponse(Long pageId,
                                long midCategoryId,
                                List<QuestionResponse> questions) {

}
