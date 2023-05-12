package com.aurora.newMeans.restaurant.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 前台首页 点击文章 跳转后 显示所有的菜单
 * 返回的每个菜单的信息
 */
@Data
@Builder
public class DinIngVo {
    private String title;
    private String imgSrc;
    private String price;
    private String desc;
    private LocalDateTime createTime;
}
