package com.aurora.constant;

/**
 * redis常用配置
 */
public interface RedisConstant {

    long CODE_EXPIRE_TIME = 15 * 60;

    String USER_CODE_KEY = "code:";

    String BLOG_VIEWS_COUNT = "blog_views_count";

    String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * 获取网站配置信息
     */
    String WEBSITE_CONFIG = "website_config";


    /**
     * 关于我的信息
     */
    String ABOUT = "about";


    String LOGIN_USER = "login_user";

    String ARTICLE_ACCESS = "article_access:";

}
