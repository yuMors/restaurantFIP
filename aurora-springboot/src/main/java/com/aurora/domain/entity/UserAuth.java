package com.aurora.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息
 * 演示账号信息
 * admin@163.com
 * 123456
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_auth")
public class UserAuth {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户信息id
     */
    private Integer userInfoId;

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 登录类型
     */
    private Integer loginType;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
