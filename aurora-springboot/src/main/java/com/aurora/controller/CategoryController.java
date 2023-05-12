package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.CategoryAdminDTO;
import com.aurora.domain.dto.CategoryDTO;
import com.aurora.domain.dto.CategoryOptionDTO;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.vo.ResultVo;
import com.aurora.service.CategoryService;
import com.aurora.domain.vo.CategoryVO;
import com.aurora.domain.vo.ConditionVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 前台
     * 获取所有分类
     */
    @ApiOperation("获取所有分类")
    @GetMapping("/categories/all")
    public ResultVo<List<CategoryDTO>> listCategories() {
        return ResultVo.ok(categoryService.listCategories());
    }

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/categories")
    public ResultVo<PageResultDto<CategoryAdminDTO>> listCategoriesAdmin(ConditionVO conditionVO) {
        return ResultVo.ok(categoryService.listCategoriesAdmin(conditionVO));
    }

    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/categories/search")
    public ResultVo<List<CategoryOptionDTO>> listCategoriesAdminBySearch(ConditionVO conditionVO) {
        return ResultVo.ok(categoryService.listCategoriesBySearch(conditionVO));
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    public ResultVo<?> deleteCategories(@RequestBody List<Integer> categoryIds) {
        categoryService.deleteCategories(categoryIds);
        return ResultVo.ok();
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/admin/categories")
    public ResultVo<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdateCategory(categoryVO);
        return ResultVo.ok();
    }


}
