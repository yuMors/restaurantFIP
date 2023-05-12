package com.aurora.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论dto
 * 这个应该就是一级评论
 * @author 30676
 * @date 2023-04-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    /**
     * id
     */
    private Integer id;
    /**用户id*/
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    //网站 弃用
    //private String webSite;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 回复dto
     */
    private List<ReplyDTO> replyDTOs;

}
