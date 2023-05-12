package com.aurora.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论
 * @author 30676
 * @datetime 2023-03-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 回复用户id
     */
    private Integer replyUserId;

    /**
     * 主题id
     */
    private Integer topicId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 是删除
     */
    private Integer isDelete;

    /**
     * 是否审核 0否 1是
     * 这个字段先不启用
     */
    private Integer isReview;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
