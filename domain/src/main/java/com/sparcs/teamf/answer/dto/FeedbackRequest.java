package com.sparcs.teamf.answer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record FeedbackRequest(@Positive @NotNull Long questionId,
                              @NotEmpty String answer) {

}
