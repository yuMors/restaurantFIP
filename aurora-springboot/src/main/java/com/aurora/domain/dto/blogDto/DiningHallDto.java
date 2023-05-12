package com.aurora.domain.dto.blogDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 餐厅返回类
 */
@Setter
@Getter
@Builder
public class DiningHallDto {
    private String img;
    private String title;
    private String price;
    private String desc;
}
