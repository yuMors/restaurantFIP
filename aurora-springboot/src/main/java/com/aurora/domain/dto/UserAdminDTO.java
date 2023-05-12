package com.aurora.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminDTO {

    private Integer id;

    private Integer userInfoId;

    private String avatar;

    private String nickname;

    private String username;

    private List<UserRoleDTO> roles;

    private Date createTime;

    private Date lastLoginTime;

//    private Integer status;

    private String intro;

}
