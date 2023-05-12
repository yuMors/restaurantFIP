package com.aurora.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_info")
public class UserInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 手机号 账号 用户名
     */
    private String username;
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户类型
     * 暂定1为普通用户 未定义或为空也定义为普通用户
     * 2为餐厅用户 3为管理员
     */
    private Integer userType;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 邮箱 暂时不用了
     */
    private String email;
    /**
     * 用户简介
     */
    private String intro;
    /**
     * 个人网站
     */
    private String website;

    /**
     * 是否订阅
     */
    private Integer isSubscribe;
    /**
     * 是否禁用
     */
    private Integer isDisable;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
