package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.RoleDTO;
import com.aurora.domain.dto.UserRoleDTO;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.domain.vo.RoleVO;
import com.aurora.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.DELETE;
import static com.aurora.constant.OptTypeConstant.SAVE_OR_UPDATE;

@Api(tags = "角色模块")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public ResultVo<List<UserRoleDTO>> listUserRoles() {
        return ResultVo.ok(roleService.listUserRoles());
    }

    /**
     * 查询角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public ResultVo<PageResultDto<RoleDTO>> listRoles(ConditionVO conditionVO) {
        return ResultVo.ok(roleService.listRoles(conditionVO));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public ResultVo<?> saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return ResultVo.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public ResultVo<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return ResultVo.ok();
    }
}
