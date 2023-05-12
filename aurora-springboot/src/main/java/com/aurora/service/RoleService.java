package com.aurora.service;

import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.RoleDTO;
import com.aurora.domain.dto.UserRoleDTO;
import com.aurora.domain.entity.Role;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.RoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<UserRoleDTO> listUserRoles();

    /**
     * 查询角色列表
     */
    PageResultDto<RoleDTO> listRoles(ConditionVO conditionVO);

    void saveOrUpdateRole(RoleVO roleVO);

    void deleteRoles(List<Integer> ids);

}
