package com.aurora.service;

import com.aurora.domain.dto.LabelOptionDTO;
import com.aurora.domain.dto.MenuDTO;
import com.aurora.domain.dto.UserMenuDTO;
import com.aurora.domain.entity.Menu;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.IsHiddenVO;
import com.aurora.domain.vo.MenuVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<MenuDTO> listMenus(ConditionVO conditionVO);

    void saveOrUpdateMenu(MenuVO menuVO);

    void updateMenuIsHidden(IsHiddenVO isHiddenVO);

    void deleteMenu(Integer menuId);

    List<LabelOptionDTO> listMenuOptions();

    /**
     * 查看当前用户菜单
     */
    List<UserMenuDTO> listUserMenus();

}
