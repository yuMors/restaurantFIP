package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    STUDENT(3,"学生用户，普通用户",""),

    EMAIL(1, "邮箱登录", ""),

    QQ(2, "QQ登录", "qqLoginStrategyImpl");

    private final Integer type;

    private final String desc;

    /**策略 应该有误*/
    private final String strategy;

}
