package com.aurora.service;

import com.aurora.domain.dto.*;
import com.aurora.domain.entity.Article;
import com.aurora.domain.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ArticleService extends IService<Article> {

    /**
     * 前台
     * 获取置顶和推荐文章
     */
    TopAndFeaturedArticlesDTO listTopAndFeaturedArticles();

    /**
     * 前台
     * 获取所有文章
     */
    PageResultDto<ArticleCardDTO> listArticles();

    PageResultDto<ArticleCardDTO> listArticlesByCategoryId(Integer categoryId);

    /**
     * 根据id获取文章
     */
    ArticleDTO getArticleById(Integer articleId);

    void accessArticle(ArticlePasswordVO articlePasswordVO);

    PageResultDto<ArticleCardDTO> listArticlesByTagId(Integer tagId);

    PageResultDto<ArchiveDTO> listArchives();

    /**
     * 获取后台文章
     */
    PageResultDto<ArticleAdminDTO> listArticlesAdmin(ConditionVO conditionVO);

    //文章添加
    void saveOrUpdateArticle(ArticleVO articleVO);

    void updateArticleTopAndFeatured(ArticleTopFeaturedVO articleTopFeaturedVO);

    void updateArticleDelete(DeleteVO deleteVO);

    void deleteArticles(List<Integer> articleIds);

    ArticleAdminViewDTO getArticleByIdAdmin(Integer articleId);


    List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition);

}
