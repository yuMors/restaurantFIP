package com.aurora.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章
 *
 * @author 30676
 * @date 2023/03/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_article")
public class Article {

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
     * 类别id
     */
    private Integer categoryId;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 菜单价格
     */
    private String diningPrice;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 最高
     */
    private Integer isTop;

    /**
     * 是否推荐 0否 1是
     */
    private Integer isFeatured;

    /**
     * 是删除
     */
    private Integer isDelete;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 密码 弃用
     */
    private String password;
    /**
     * 上级id 存用户id就行 info id
     */
    private Integer pid;

    /**
     * 原始url 弃用 改成 描述或简介
     */
    private String cpDesc;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
