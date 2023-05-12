package com.aurora.mapper;

import com.aurora.domain.dto.UserAdminDTO;
import com.aurora.domain.entity.UserAuth;
import com.aurora.domain.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Repository
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    List<UserAdminDTO> listUsers(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

    Integer countUser(@Param("conditionVO") ConditionVO conditionVO);

    /*
    SELECT role_name FROM t_role WHERE id = (SELECT id  FROM t_user_info WHERE id = (SELECT user_info_id FROM t_user_auth WHERE id = '1014'))

    SELECT tr.role_name FROM t_role AS tr
    INNER JOIN t_user_info AS info ON tr.id = info.role_id
    INNER JOIN t_user_auth AS auth ON auth.user_info_id = info.id
    WHERE auth.id='1014'
     */
    @Select("SELECT tr.role_name FROM t_role tr,t_user_info info,t_user_auth auth WHERE info.role_id = tr.id AND auth.user_info_id = info.id AND auth.id=#{userAuthId}")
    String getUserRole(Integer userAuthId);

    @Select("select user_info_id from t_user_auth where id = #{userAuthId}")
    Integer getUserId(Integer userAuthId);

    @Select("SELECT category_name FROM t_category WHERE id = #{categoryId} ")
    String getCategoryName(Integer categoryId);

    @Delete("delete from t_user_auth where user_info_id = #{id}")
    void deleteByUserInfoId(String id);
}
