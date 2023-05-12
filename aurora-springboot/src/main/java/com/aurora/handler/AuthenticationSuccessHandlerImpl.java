package com.aurora.handler;

import com.alibaba.fastjson.JSON;
import com.aurora.constant.CommonConstant;
import com.aurora.domain.dto.UserDetailsDTO;
import com.aurora.domain.dto.UserInfoDTO;
import com.aurora.domain.entity.UserAuth;
import com.aurora.domain.vo.ResultVo;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.service.TokenService;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录时创建的token
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("位置 AuthenticationSuccessHandlerImpl: authentication " + authentication);
        UserInfoDTO userLoginDTO = BeanCopyUtil.copyObject(UserUtil.getUserDetailsDTO(), UserInfoDTO.class);
        System.out.println("位置 AuthenticationSuccessHandlerImpl 登录时创建的token userLoginDTO \n"+userLoginDTO);
        if (Objects.nonNull(authentication)) {
            UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
            String token = tokenService.createToken(userDetailsDTO);
            userLoginDTO.setToken(token);
        }
        response.setContentType(CommonConstant.APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResultVo.ok(userLoginDTO)));
        updateUserInfo();
    }

    @Async
    public void updateUserInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtil.getUserDetailsDTO().getId())
                .build();
        userAuthMapper.updateById(userAuth);
    }
}