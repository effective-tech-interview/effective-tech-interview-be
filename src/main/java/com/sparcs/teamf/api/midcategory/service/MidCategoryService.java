package com.sparcs.teamf.api.midcategory.service;

import com.sparcs.teamf.api.maincategory.exception.MainCategoryNotFoundException;
import com.sparcs.teamf.api.midcategory.dto.MidCategoriesResponse;
import com.sparcs.teamf.api.midcategory.dto.MidCategoryResponse;
import com.sparcs.teamf.api.midcategory.exception.MidCategoryNotFoundException;
import com.sparcs.teamf.domain.maincategory.MainCategory;
import com.sparcs.teamf.domain.maincategory.MainCategoryRepository;
import com.sparcs.teamf.domain.midcategory.MidCategory;
import com.sparcs.teamf.domain.midcategory.MidCategoryRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
