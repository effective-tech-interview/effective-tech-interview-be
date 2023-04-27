package com.sparcs.teamf.midcategory.service;

import com.sparcs.teamf.maincategory.MainCategory;
import com.sparcs.teamf.maincategory.MainCategoryRepository;
import com.sparcs.teamf.maincategory.exception.MainCategoryNotFoundException;
import com.sparcs.teamf.midcategory.MidCategory;
import com.sparcs.teamf.midcategory.MidCategoryRepository;
import com.sparcs.teamf.midcategory.dto.MidCategoriesResponse;
import com.sparcs.teamf.midcategory.dto.MidCategoryResponse;
import com.sparcs.teamf.midcategory.exception.MidCategoryNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MidCategoryService {

    private final MainCategoryRepository mainCategoryRepository;
    private final MidCategoryRepository midCategoryRepository;

    @Transactional
    public MidCategoriesResponse getByMainCategory(Long mainCategoryId) {
        MainCategory mainCategory = mainCategoryRepository.findById(mainCategoryId)
                .orElseThrow(MainCategoryNotFoundException::new);

        List<MidCategoryResponse> midCategoryResponses = mainCategory.getMidCategories().stream()
                .map(midCategory -> new MidCategoryResponse(midCategory.getId(), midCategory.getName(),
                        midCategory.getImageUrl()))
                .toList();

        return new MidCategoriesResponse(mainCategory.getName(), midCategoryResponses);
    }

    @Transactional
    public MidCategoryResponse getByMidCategoryId(long midCategoryId) {
        MidCategory midCategory = midCategoryRepository.findById(midCategoryId)
                .orElseThrow(MidCategoryNotFoundException::new);
        return new MidCategoryResponse(midCategory.getId(), midCategory.getName(), midCategory.getImageUrl());
    }
}
