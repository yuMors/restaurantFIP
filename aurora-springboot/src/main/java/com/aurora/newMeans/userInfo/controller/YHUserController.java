package com.aurora.newMeans.userInfo.controller;

import com.aurora.domain.vo.ResultVo;
import com.aurora.newMeans.userInfo.domain.YHUser;
import com.aurora.newMeans.userInfo.domain.YHUserDto;
import com.aurora.newMeans.userInfo.domain.YHUserVo;
import com.aurora.newMeans.userInfo.service.YHUserService;
import com.aurora.util.qiniuYunOss.config.QiniuYunOssService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class YHUserController {

    @Autowired
    private YHUserService userService;
    @Autowired
    private QiniuYunOssService qiniuUploadService;


    @GetMapping("/userList")
    public ResultVo<?> userList(@RequestParam(name = "current") Integer current,
                                @RequestParam(name = "size") Integer size) {
        LambdaQueryWrapper<YHUser> queryWrapper = new LambdaQueryWrapper<>();
        Page<YHUser> page = new Page<>(current, size);
        IPage<YHUser> userInfosPage = userService.page(page, queryWrapper);
        List<YHUserVo> userVos = new ArrayList<>();
        userInfosPage.getRecords().forEach(user -> {
            YHUserVo build = YHUserVo.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .userType(user.getUserType())
                    .userImg(user.getAvatar())
                    .username(user.getUsername())
                    .createTime(user.getCreateTime()).build();
            userVos.add(build);
        });
        IPage<YHUserVo> dtoPage = new Page<>(current, size, userVos.size());
        dtoPage.setRecords(userVos);
        return ResultVo.ok(dtoPage.getRecords());
    }

    /**
     *
     * @param file
     * @return 图片http链接
     */
    @PostMapping("/uploadImg")
    public ResultVo<?> getImg(MultipartFile file){
        String s = qiniuUploadService.uploadImg(file);
        return ResultVo.ok(s);
    }

    @PostMapping("/add")
    public ResultVo<?> addUser(@RequestBody YHUserDto userDto){
        userService.handleAdd(userDto);
        //String s = qiniuUploadService.uploadImg(file);
        return ResultVo.ok();
    }
}


