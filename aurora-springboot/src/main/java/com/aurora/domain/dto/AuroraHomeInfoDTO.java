package com.aurora.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 极光dto回家信息
 *
 * @author 30676
 * @date 2023-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuroraHomeInfoDTO {

    /**
     * 条数
     */
    private Integer articleCount;

    /**
     * 讨论数
     */
    private Integer talkCount;

    /**
     * 类别数
     */
    private Integer categoryCount;

    /**
     * 标签数
     */
    private Integer tagCount;

    /**
     * 网站配置dto
     */
    private WebsiteConfigDTO websiteConfigDTO;

    /**
     * 视图数
     */
    private Integer viewCount;

}
