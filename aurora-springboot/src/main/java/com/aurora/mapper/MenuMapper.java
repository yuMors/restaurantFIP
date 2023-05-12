package com.aurora.mapper;

import com.aurora.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 所拥有的菜单权限信息
     *
     * @param userInfoId 作用是获取指定用户（userInfoId）
     */
    //List<Menu> listMenusByUserInfoId(Integer userInfoId);
    @Select("select role_id from t_user_info where id = #{userInfoId}")
    Integer getRoleId(Integer userInfoId);

    @Select("select menu_id from t_role_menu where role_id = #{roleId}")
    List<Integer> getRoleMenu(Integer roleId);
}
