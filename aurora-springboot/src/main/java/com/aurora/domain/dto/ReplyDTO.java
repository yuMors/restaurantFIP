package com.aurora.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 回复dto
 * 这个应该就是子级评论
 * @author 30676
 * @date 2023-04-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    /**
     * id 评论表comment的id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 父id
     */
    private Integer parentId;

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
     * 回复用户id
     */
    private Integer replyUserId;

    /**
     * 回复昵称
     */
    private String replyNickname;

    //回复网站  弃用
    //private String replyWebsite;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
