package com.aurora.mapper;

import com.aurora.domain.dto.TagAdminDTO;
import com.aurora.domain.dto.TagDTO;
import com.aurora.domain.entity.Tag;
import com.aurora.domain.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<TagDTO> listTags();

    /**
     * 前台
     * 获取前十个标签
     */
    List<TagDTO> listTopTenTags();

    List<String> listTagNamesByArticleId(Integer articleId);

    List<TagAdminDTO> listTagsAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

}
