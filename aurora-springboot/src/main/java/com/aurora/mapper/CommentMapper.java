package com.aurora.mapper;

import com.aurora.domain.dto.CommentAdminDTO;
import com.aurora.domain.dto.CommentCountDTO;
import com.aurora.domain.dto.CommentDTO;
import com.aurora.domain.dto.ReplyDTO;
import com.aurora.domain.entity.Comment;
import com.aurora.domain.vo.CommentVO;
import com.aurora.domain.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 获取评论
     */
    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size, @Param("commentVO") CommentVO commentVO);

    List<ReplyDTO> listReplies(@Param("commentIds") List<Integer> commentIdList);

    /**
     * 前台 进去初始化的时候加载的
     * 获取前六个评论
     */
    List<CommentDTO> listTopSixComments();

    Integer countComments(@Param("conditionVO") ConditionVO conditionVO);

    List<CommentAdminDTO> listCommentsAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

    List<CommentCountDTO> listCommentCountByTypeAndTopicIds(@Param("type") Integer type, @Param("topicIds") List<Integer> topicIds);

    CommentCountDTO listCommentCountByTypeAndTopicId(@Param("type") Integer type, @Param("topicId") Integer topicId);

}
