package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.UserInfoDTO;
import com.aurora.domain.dto.UserOnlineDTO;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.service.UserAuthService;
import com.aurora.service.UserInfoService;
import com.aurora.domain.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.aurora.constant.OptTypeConstant.DELETE;
import static com.aurora.constant.OptTypeConstant.UPDATE;

@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAuthMapper userAuthMapper;

    @OptLog(optType = UPDATE)
    @ApiOperation("更新用户信息")
    @PutMapping("/users/info")
    public ResultVo<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return ResultVo.ok();
    }

    /**
     * 更新用户头像
     */
    @OptLog(optType = UPDATE)
    @ApiOperation("更新用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    public ResultVo<String> updateUserAvatar(MultipartFile file) {
        return ResultVo.ok(userInfoService.updateUserAvatar(file));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("绑定用户邮箱")
    @PutMapping("/users/email")
    public ResultVo<?> saveUserEmail(@Valid @RequestBody EmailVO emailVO) {
        userInfoService.saveUserEmail(emailVO);
        return ResultVo.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("修改用户的订阅状态")
    @PutMapping("/users/subscribe")
    public ResultVo<?> updateUserSubscribe(@RequestBody SubscribeVO subscribeVO) {
        userInfoService.updateUserSubscribe(subscribeVO);
        return ResultVo.ok();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @GetMapping("/admin/userDelete")
    public ResultVo<?> userDelete(@RequestParam(value = "id") String id) {
        userInfoService.removeById(id);
        //同时删除权限表里面的相关用户信息
        userAuthMapper.deleteByUserInfoId(id);
        return ResultVo.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    public ResultVo<?> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userInfoService.updateUserRole(userRoleVO);
        return ResultVo.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable")
    public ResultVo<?> updateUserDisable(@Valid @RequestBody UserDisableVO userDisableVO) {
        userInfoService.updateUserDisable(userDisableVO);
        return ResultVo.ok();
    }

    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/users/online")
    public ResultVo<PageResultDto<UserOnlineDTO>> listOnlineUsers(ConditionVO conditionVO) {
        return ResultVo.ok(userInfoService.listOnlineUsers(conditionVO));
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "下线用户")
    @DeleteMapping("/admin/users/{userInfoId}/online")
    public ResultVo<?> removeOnlineUser(@PathVariable("userInfoId") Integer userInfoId) {
        userInfoService.removeOnlineUser(userInfoId);
        return ResultVo.ok();
    }

    @ApiOperation("根据id获取用户信息")
    @GetMapping("/users/info/{userInfoId}")
    public ResultVo<UserInfoDTO> getUserInfoById(@PathVariable("userInfoId") Integer userInfoId) {
        return ResultVo.ok(userInfoService.getUserInfoById(userInfoId));
    }

}
