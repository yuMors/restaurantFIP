package com.aurora.service;

import com.aurora.domain.dto.CommentAdminDTO;
import com.aurora.domain.dto.CommentDTO;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.ReplyDTO;
import com.aurora.domain.entity.Comment;
import com.aurora.domain.entity.UserInfo;
import com.aurora.domain.vo.CommentVO;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.ReviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     */
    void saveComment(CommentVO commentVo);

    /**
     * 获取评论
     */
    PageResultDto<CommentDTO> listComments(CommentVO commentVo, Long current, Long size);

    /**
     * 根据commentId获取回复
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId);

    /**
     * 前台 进去初始化的时候加载的
     * 获取前六个评论
     */
    List<CommentDTO> listTopSixComments();

    PageResultDto<CommentAdminDTO> listCommentsAdmin(ConditionVO conditionVO);

    void updateCommentsReview(ReviewVO reviewVO);
    /**
     * 这个应该是查看子评论
     * 根据评论者的id查询所有评论信息
     * @param commentIds 重写的
     */
    List<ReplyDTO> listRepliesYH(List<Integer> commentIds);

    /**
     * 根据用户id得到用户信息
     */
    UserInfo getUserInfo(Integer userId);



}
