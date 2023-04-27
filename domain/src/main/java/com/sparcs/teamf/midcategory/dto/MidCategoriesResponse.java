package com.sparcs.teamf.midcategory.dto;

import java.util.List;

public record MidCategoriesResponse(String name,
                                    List<MidCategoryResponse> categories) {

}
