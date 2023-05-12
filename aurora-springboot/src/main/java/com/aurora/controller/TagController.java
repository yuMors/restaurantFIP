package com.aurora.controller;


import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.TagAdminDTO;
import com.aurora.domain.dto.TagDTO;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.domain.vo.TagVO;
import com.aurora.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.DELETE;
import static com.aurora.constant.OptTypeConstant.SAVE_OR_UPDATE;

@Api(tags = "标签模块")
@RestController
public class TagController {


    @Autowired
    private TagService tagService;
    /**
     * 前台
     * 获取前十个标签
     */
    @ApiOperation("获取前十个标签")
    @GetMapping("/tags/topTen")
    public ResultVo<List<TagDTO>> getTopTenTags() {
        return ResultVo.ok(tagService.listTopTenTags());
    }

    @ApiOperation("获取所有标签")
    @GetMapping("/tags/all")
    public ResultVo<List<TagDTO>> getAllTags() {
        return ResultVo.ok(tagService.listTags());
    }


    @ApiOperation(value = "查询后台标签列表")
    @GetMapping("/admin/tags")
    public ResultVo<PageResultDto<TagAdminDTO>> listTagsAdmin(ConditionVO conditionVO) {
        return ResultVo.ok(tagService.listTagsAdmin(conditionVO));
    }

    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/admin/tags/search")
    public ResultVo<List<TagAdminDTO>> listTagsAdminBySearch(ConditionVO condition) {
        return ResultVo.ok(tagService.listTagsAdminBySearch(condition));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tags")
    public ResultVo<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return ResultVo.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    public ResultVo<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return ResultVo.ok();
    }
}
