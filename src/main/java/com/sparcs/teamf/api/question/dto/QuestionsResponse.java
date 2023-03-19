package com.sparcs.teamf.api.question.dto;

import java.util.List;

public record QuestionsResponse(Long pageId,
                                List<QuestionResponse> questions) {

}
