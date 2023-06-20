package com.sparcs.teamf.page.dto;

import java.util.List;

public record QuestionsResponse(Long pageId,
                                long midCategoryId,
                                List<QuestionResponse> questions) {

}
