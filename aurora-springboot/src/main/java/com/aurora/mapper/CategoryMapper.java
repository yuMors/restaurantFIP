package com.aurora.mapper;

import com.aurora.domain.dto.CategoryAdminDTO;
import com.aurora.domain.dto.CategoryDTO;
import com.aurora.domain.entity.Category;
import com.aurora.domain.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 前台
     * 获取所有分类
     */
    List<CategoryDTO> listCategories();

    List<CategoryAdminDTO> listCategoriesAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

}
