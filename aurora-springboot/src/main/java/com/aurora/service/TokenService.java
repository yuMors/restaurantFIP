package com.aurora.service;

import com.aurora.domain.dto.UserDetailsDTO;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {

    /**
     * 登录时创建的token
     */
    String createToken(UserDetailsDTO userDetailsDTO);

    /**
     * 登录时创建的token
     */
    String createToken(String subject);

    void refreshToken(UserDetailsDTO userDetailsDTO);

    void renewToken(UserDetailsDTO userDetailsDTO);

    Claims parseToken(String token);

    UserDetailsDTO getUserDetailDTO(HttpServletRequest request);

    void delLoginUser(Integer userId);

}
