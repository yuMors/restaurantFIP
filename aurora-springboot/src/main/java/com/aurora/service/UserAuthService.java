package com.aurora.service;

import com.aurora.domain.dto.*;
import com.aurora.domain.vo.*;

import java.util.List;

public interface UserAuthService {

    void register(UserVO userVO);

    void updatePassword(UserVO userVO);

    void updateAdminPassword(PasswordVO passwordVO);

    /**
     * 查询后台用户列表
     */
    PageResultDto<UserAdminDTO> listUsers(ConditionVO condition);

    UserLogoutStatusDTO logout();



}
