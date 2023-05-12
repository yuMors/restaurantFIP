package com.aurora.service.impl;

import com.aurora.constant.CommonConstant;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.RoleDTO;
import com.aurora.domain.dto.UserRoleDTO;
import com.aurora.domain.entity.Role;
import com.aurora.domain.entity.RoleMenu;
import com.aurora.domain.entity.RoleResource;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.RoleVO;
import com.aurora.exception.BizException;
import com.aurora.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.aurora.mapper.RoleMapper;
import com.aurora.newMeans.userInfo.domain.YHUser;
import com.aurora.newMeans.userInfo.mapper.YHUserMapper;
import com.aurora.service.RoleMenuService;
import com.aurora.service.RoleResourceService;
import com.aurora.service.RoleService;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private YHUserMapper userMapper;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleDTO> listUserRoles() {
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
        return BeanCopyUtil.copyList(roleList, UserRoleDTO.class);
    }

    /**
     * 查询角色列表
     * 只有管理员可以
     */
    @SneakyThrows
    @Override
    public PageResultDto<RoleDTO> listRoles(ConditionVO conditionVo) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Role::getRoleName, conditionVo.getKeywords());
        Integer asyncCount = roleMapper.selectCount(queryWrapper);
        List<RoleDTO> roleDTOs = roleMapper.listRoles(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVo);
        Page<Role> page = new Page<>(conditionVo.getCurrent(), conditionVo.getSize());
        Page<Role> pageList = this.page(page);
        List<Integer> collectId = pageList.getRecords().stream().map(Role::getId).collect(Collectors.toList());

        return new PageResultDto<>(roleDTOs, asyncCount);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        Role roleCheck = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVO.getRoleName()));
        if (Objects.nonNull(roleCheck) && !(roleCheck.getId().equals(roleVO.getId()))) {
            throw new BizException("该角色存在");
        }
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .isDisable(CommonConstant.FALSE)
                .build();
        this.saveOrUpdate(role);
        if (Objects.nonNull(roleVO.getResourceIds())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }
            List<RoleResource> roleResourceList = roleVO.getResourceIds().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            filterInvocationSecurityMetadataSource.clearDataSource();
        }
        if (Objects.nonNull(roleVO.getMenuIds())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleVO.getId()));
            }
            List<RoleMenu> roleMenuList = roleVO.getMenuIds().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<YHUser>()
                .in(YHUser::getRoleId, roleIdList));
        if (count > 0) {
            throw new BizException("该角色下存在用户");
        }
        roleMapper.deleteBatchIds(roleIdList);
    }

}
