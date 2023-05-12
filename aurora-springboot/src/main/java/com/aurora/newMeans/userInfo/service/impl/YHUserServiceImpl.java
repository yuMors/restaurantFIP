package com.aurora.newMeans.userInfo.service.impl;

import com.aurora.domain.entity.UserAuth;
import com.aurora.enums.RoleEnum;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.newMeans.userInfo.domain.YHUser;
import com.aurora.newMeans.userInfo.domain.YHUserDto;
import com.aurora.newMeans.userInfo.mapper.YHUserMapper;
import com.aurora.newMeans.userInfo.service.YHUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class YHUserServiceImpl extends ServiceImpl<YHUserMapper, YHUser> implements YHUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private YHUserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;


    @Override
    public void handleAdd(YHUserDto userDto) {
        if (userDto.getPassword().isEmpty()){
            userDto.setPassword("123456");
        }
        System.out.println("加密前的密码："+userDto.getPassword());
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        System.out.println("加密后的密码："+encodePassword);

        YHUser user = YHUser.builder()
                .nickname(userDto.getNickname())
                .avatar(userDto.getAvatar())
                .password(encodePassword)
                .username(userDto.getUsername())
                .roleId(15)
                .build();
        userMapper.insert(user);

        YHUser selectOne = userMapper.selectOne(new LambdaQueryWrapper<YHUser>()
                .eq(YHUser::getNickname, userDto.getNickname()));

        UserAuth userAuth = UserAuth.builder()
                .userInfoId(selectOne.getId())
                .username(userDto.getUsername())
                .password(encodePassword)
                .loginType(1)
                .build();
        userAuthMapper.insert(userAuth);

    }
}
