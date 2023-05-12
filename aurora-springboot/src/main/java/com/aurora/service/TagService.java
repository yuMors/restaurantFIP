package com.aurora.service;

import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.TagAdminDTO;
import com.aurora.domain.dto.TagDTO;
import com.aurora.domain.entity.Tag;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<TagDTO> listTags();

    /**
     * 前台
     * 获取前十个标签
     */
    List<TagDTO> listTopTenTags();

    PageResultDto<TagAdminDTO> listTagsAdmin(ConditionVO conditionVO);

    List<TagAdminDTO> listTagsAdminBySearch(ConditionVO conditionVO);

    void saveOrUpdateTag(TagVO tagVO);

    void deleteTag(List<Integer> tagIds);

}
