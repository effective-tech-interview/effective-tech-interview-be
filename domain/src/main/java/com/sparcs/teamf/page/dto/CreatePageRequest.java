package com.sparcs.teamf.page.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CreatePageRequest(@NotNull @Positive Long midCategoryId) {

}
