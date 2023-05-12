package com.aurora.newMeans.userInfo.service;

import com.aurora.newMeans.userInfo.domain.YHUser;
import com.aurora.newMeans.userInfo.domain.YHUserDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface YHUserService extends IService<YHUser> {
    void handleAdd(YHUserDto userDto);
}
