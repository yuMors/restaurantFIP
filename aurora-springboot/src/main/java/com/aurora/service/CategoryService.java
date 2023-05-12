package com.aurora.service;

import com.aurora.domain.dto.CategoryAdminDTO;
import com.aurora.domain.dto.CategoryDTO;
import com.aurora.domain.dto.CategoryOptionDTO;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.entity.Category;
import com.aurora.domain.vo.CategoryVO;
import com.aurora.domain.vo.ConditionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 前台
     * 获取所有分类
     */
    List<CategoryDTO> listCategories();

    PageResultDto<CategoryAdminDTO> listCategoriesAdmin(ConditionVO conditionVO);

    List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO conditionVO);

    void deleteCategories(List<Integer> categoryIds);

    void saveOrUpdateCategory(CategoryVO categoryVO);

}
