package com.aurora.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.domain.dto.*;
import com.aurora.domain.entity.Article;
import com.aurora.domain.entity.Comment;
import com.aurora.domain.entity.WebsiteConfig;
import com.aurora.domain.vo.WebsiteConfigVO;
import com.aurora.mapper.*;
import com.aurora.service.AuroraInfoService;
import com.aurora.service.RedisService;
import com.aurora.service.UniqueViewService;
import com.aurora.util.BeanCopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aurora.constant.CommonConstant.*;
import static com.aurora.constant.RedisConstant.*;

@Service
public class AuroraInfoServiceImpl implements AuroraInfoService {

    @Autowired
    private WebsiteConfigMapper websiteConfigMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private CommentMapper commentMapper;



    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UniqueViewService uniqueViewService;



    /**
     * 前台
     * 获取系统信息
     */
    @SneakyThrows
    @Override
    public AuroraHomeInfoDTO getAuroraHomeInfo() {
        System.out.println("api/");
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .isNull(Article::getPid));
        Integer categoryCount = categoryMapper.selectCount(null);
        Integer tagCount = tagMapper.selectCount(null);
//        Integer talkCount = talkMapper.selectCount(null);
        Integer talkCount = articleMapper.getCommentCount();
        WebsiteConfigDTO asyncWebsiteConfig = this.getWebsiteConfig();
        CompletableFuture<Integer> asyncViewCount = CompletableFuture.supplyAsync(
                () -> {
                    Object count = redisService.get(BLOG_VIEWS_COUNT);
                    return Integer.parseInt(Optional.ofNullable(count).orElse(0).toString());
                });
        return AuroraHomeInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .talkCount(talkCount)
                .websiteConfigDTO(asyncWebsiteConfig)
                .viewCount(asyncViewCount.get()).build();
    }

    /**to
     * 获取网站配置信息
     */
    @Override
    public WebsiteConfigDTO getWebsiteConfig() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication: "+"位置: AuroraInfoServiceImpl");
        System.err.println(authentication);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("principal: "+"位置: AuroraInfoServiceImpl");
        System.err.println(principal);
        WebsiteConfigDTO websiteConfigDto;
        Object websiteConfig = redisService.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigDto = JSON.parseObject(websiteConfig.toString(), WebsiteConfigDTO.class);
        } else {
            String config = websiteConfigMapper.selectById(DEFAULT_CONFIG_ID).getConfig();
            websiteConfigDto = JSON.parseObject(config, WebsiteConfigDTO.class);
            redisService.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigDto;
    }

    @Override
    public AuroraAdminInfoDTO getAuroraAdminInfo() {
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        Integer viewsCount = Integer.parseInt(Optional.ofNullable(count).orElse(0).toString());
        Integer messageCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getType, 2));
        Integer userCount = userInfoMapper.selectCount(null);
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE));
        List<UniqueViewDTO> uniqueViews = uniqueViewService.listUniqueViews();
        List<ArticleStatisticsDTO> articleStatisticsDTOs = articleMapper.listArticleStatistics();
        List<CategoryDTO> categoryDTOs = categoryMapper.listCategories();
        List<TagDTO> tagDTOs = BeanCopyUtil.copyList(tagMapper.selectList(null), TagDTO.class);
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0, 4);
        AuroraAdminInfoDTO auroraAdminInfoDTO = AuroraAdminInfoDTO.builder()
                .articleStatisticsDTOs(articleStatisticsDTOs)
                .tagDTOs(tagDTOs)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOs(categoryDTOs)
                .uniqueViewDTOs(uniqueViews)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            List<ArticleRankDTO> articleRankDTOList = listArticleRank(articleMap);
            auroraAdminInfoDTO.setArticleRankDTOs(articleRankDTOList);
        }
        return auroraAdminInfoDTO;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(DEFAULT_CONFIG_ID)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigMapper.updateById(websiteConfig);
        redisService.del(WEBSITE_CONFIG);
    }




    private List<ArticleRankDTO> listArticleRank(Map<Object, Double> articleMap) {
        List<Integer> articleIds = new ArrayList<>(articleMap.size());
        articleMap.forEach((key, value) -> articleIds.add((Integer) key));
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIds))
                .stream().map(article -> ArticleRankDTO.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }

}
