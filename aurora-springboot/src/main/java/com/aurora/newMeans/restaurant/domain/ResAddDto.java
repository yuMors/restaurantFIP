package com.aurora.newMeans.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto接 vo返回
 * 后台 菜谱新增的时候传到后端的对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResAddDto {
    private Integer id;
    private String cpName;
    private String cpPrice;
    private String cpDesc;

    private String avatar;
    /**
     * 对应 auth 里面的id
     */
    private Integer userAuthId;

}
