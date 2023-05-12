package com.aurora.mapper;

import com.aurora.domain.dto.ArticleAdminDTO;
import com.aurora.domain.dto.ArticleCardDTO;
import com.aurora.domain.dto.ArticleDTO;
import com.aurora.domain.dto.ArticleStatisticsDTO;
import com.aurora.domain.entity.Article;
import com.aurora.domain.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 前台
     * 获取置顶和推荐文章
     */
    List<ArticleCardDTO> listTopAndFeaturedArticles();

    /** 前台
     * 获取文章
     * @param current 当前页
     * @param size 条数
     */
    List<ArticleCardDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    List<ArticleCardDTO> getArticlesByCategoryId(@Param("current") Long current, @Param("size") Long size, @Param("categoryId") Integer categoryId);

    ArticleDTO getArticleById(@Param("articleId") Integer articleId);

    ArticleCardDTO getPreArticleById(@Param("articleId") Integer articleId);

    ArticleCardDTO getNextArticleById(@Param("articleId") Integer articleId);

    ArticleCardDTO getFirstArticle();

    ArticleCardDTO getLastArticle();

    List<ArticleCardDTO> listArticlesByTagId(@Param("current") Long current, @Param("size") Long size, @Param("tagId") Integer tagId);

    List<ArticleCardDTO> listArchives(@Param("current") Long current, @Param("size") Long size);

    Integer countArticleAdmins(@Param("conditionVO") ConditionVO conditionVO);

    /**
     * 获取后台文章
     */
    List<ArticleAdminDTO> listArticlesAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

    List<ArticleStatisticsDTO> listArticleStatistics();

    @Select("SELECT pid FROM t_article WHERE id = #{id} ")
    Integer findPidById(Integer id);

    @Select("select user_id from t_article where id = #{articleId}")
    Integer findUserIdById(Integer articleId);

    @Select("select category_name from t_category")
    List<String> getTypeList();

    @Select("select role_name from t_role where id = (select role_id from t_user_info where id = #{userInfoId})")
    String getRoleByUserInfoId(Integer userInfoId);

    @Select("select count(1) from t_comment")
    Integer getCommentCount();
}

