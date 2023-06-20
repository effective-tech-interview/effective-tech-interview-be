package com.sparcs.teamf.page.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record FeedbackRequest(@Positive @NotNull Long pageId,
                              @Positive @NotNull Long pageQuestionId) {

}
