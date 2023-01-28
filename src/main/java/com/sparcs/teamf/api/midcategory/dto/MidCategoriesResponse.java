package com.sparcs.teamf.api.midcategory.dto;

import java.util.List;

public record MidCategoriesResponse(String name,
                                    List<MidCategoryResponse> categories) {

}
