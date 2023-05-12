package com.aurora.service;

import com.aurora.domain.dto.AuroraAdminInfoDTO;
import com.aurora.domain.dto.AuroraHomeInfoDTO;
import com.aurora.domain.dto.WebsiteConfigDTO;
import com.aurora.domain.vo.WebsiteConfigVO;

public interface AuroraInfoService {

    /**
     * 前台
     * 获取系统信息
     */
    AuroraHomeInfoDTO getAuroraHomeInfo();

    AuroraAdminInfoDTO getAuroraAdminInfo();

    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    /**
     * 获取网站配置信息
     */
    WebsiteConfigDTO getWebsiteConfig();



}
