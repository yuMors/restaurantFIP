package com.aurora.controller;


import com.aurora.annotation.AccessLimit;
import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.UserAdminDTO;
import com.aurora.domain.dto.UserInfoDTO;
import com.aurora.domain.dto.UserLogoutStatusDTO;
import com.aurora.domain.vo.*;
import com.aurora.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.aurora.constant.OptTypeConstant.UPDATE;

@Api(tags = "用户账号模块")
@RestController
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;


    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/users")
    public ResultVo<PageResultDto<UserAdminDTO>> listUsers(ConditionVO conditionVo) {
        return ResultVo.ok(userAuthService.listUsers(conditionVo));
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/users/register")
    public ResultVo<?> register(@Valid @RequestBody UserVO userVO) {
        userAuthService.register(userVO);
        return ResultVo.ok();
    }


    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    public ResultVo<?> updatePassword(@Valid @RequestBody UserVO user) {
        userAuthService.updatePassword(user);
        return ResultVo.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    public ResultVo<?> updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userAuthService.updateAdminPassword(passwordVO);
        return ResultVo.ok();
    }

    @ApiOperation("用户登出")
    @PostMapping("/users/logout")
    public ResultVo<UserLogoutStatusDTO> logout() {
        return ResultVo.ok(userAuthService.logout());
    }



}
