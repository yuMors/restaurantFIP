package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.LabelOptionDTO;
import com.aurora.domain.dto.MenuDTO;
import com.aurora.domain.dto.UserMenuDTO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.service.MenuService;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.IsHiddenVO;
import com.aurora.domain.vo.MenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    public ResultVo<List<MenuDTO>> listMenus(ConditionVO conditionVO) {
        return ResultVo.ok(menuService.listMenus(conditionVO));
    }

    @OptLog(optType =SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/menus")
    public ResultVo<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return ResultVo.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改目录是否隐藏")
    @PutMapping("/admin/menus/isHidden")
    public ResultVo<?> updateMenuIsHidden(@RequestBody IsHiddenVO isHiddenVO) {
        menuService.updateMenuIsHidden(isHiddenVO);
        return ResultVo.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    public ResultVo<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return ResultVo.ok();
    }

    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    public ResultVo<List<LabelOptionDTO>> listMenuOptions() {
        return ResultVo.ok(menuService.listMenuOptions());
    }


    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menus")
    public ResultVo<List<UserMenuDTO>> listUserMenus() {
        return ResultVo.ok(menuService.listUserMenus());
    }
}
