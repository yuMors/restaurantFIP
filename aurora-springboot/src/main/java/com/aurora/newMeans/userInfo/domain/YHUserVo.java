package com.aurora.newMeans.userInfo.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * dto接 vo返回
 * controller层: public List<UserVO> getUsers(UserDTO userDto);
 * vo就是我们在web的controller层返回的Object，
 * 在接口中这个VO都会被转成Json对象输出，view object。
 *
 * DTO就是一个复合的DO对象，由于业务需要我们需要调用业务A查询数据得到业务对象A，
 * 再调用业务B查询数据得到业务对象B然后一系列封装转化得到复合的对象C此时他就是一个DTO，
 * data transfer object 它是一个服务层和服务层以上之间转换的对象。
 */
@Data
@Builder
public class YHUserVo {
    private Integer userId;
    private String nickname;
    /**
     * 用户类型
     * 暂定1为普通用户 未定义或为空也定义为普通用户
     * 2为餐厅用户 3为管理员
     */
    private Integer userType;
    private String userImg;
    /**
     * 手机号 账号
     */
    private String username;

    private LocalDateTime createTime;
}
