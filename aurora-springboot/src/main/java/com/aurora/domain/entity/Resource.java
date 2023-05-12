package com.aurora.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 资源
 *
 * @author 30676
 * @date 2023-03-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_resource")
public class Resource {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * url
     */
    private String url;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 是匿名
     */
    private Integer isAnonymous;

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
