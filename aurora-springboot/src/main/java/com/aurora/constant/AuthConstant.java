package com.aurora.constant;

public interface AuthConstant {

    int TWENTY_MINUTES = 20;

    /**过期时间 天 小时 分钟 秒*/
    int EXPIRE_TIME = 7 * 24 * 60 * 60;

    String TOKEN_HEADER = "Authorization";

    String TOKEN_PREFIX = "Bearer ";

    String SECRET = "huaweimian";

}
