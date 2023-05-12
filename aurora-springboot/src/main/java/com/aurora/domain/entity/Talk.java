package com.aurora.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 说话
 * 说说 可以保留
 *
 * @author 30676
 * @date 2023-03-31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_talk")

public class Talk {


    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片
     */
    private String images;

    /**
     * 最高
     */
    private Integer isTop;

    /**
     * 状态
     */
    private Integer status;

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
