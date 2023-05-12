package com.aurora.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 菜单
 * 用户菜单
 *
 * @author 30676
 * @date 2023-03-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    /**父id*/
    private Integer parentId;
    /**是隐藏*/
    private Integer isHidden;
    /**创建时间*/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**更新时间*/
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}

