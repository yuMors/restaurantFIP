package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.*;
import com.aurora.domain.entity.Article;
import com.aurora.domain.vo.*;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.newMeans.restaurant.domain.AdminDingVo;
import com.aurora.service.ArticleService;
import com.aurora.util.UserUtil;
import com.aurora.util.qiniuYunOss.config.QiniuYunOssService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private QiniuYunOssService qiniuUploadService;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Autowired
    private ArticleMapper articleMapper;


    /**
     * 前台
     * 获取置顶和推荐文章
     */
    @ApiOperation("获取置顶和推荐文章")
    @GetMapping("/articles/topAndFeatured")
    public ResultVo<TopAndFeaturedArticlesDTO> listTopAndFeaturedArticles() {
        return ResultVo.ok(articleService.listTopAndFeaturedArticles());
    }

    /**
     * 前台
     * 获取所有文章
     */
    @ApiOperation("获取所有文章")
    @GetMapping("/articles/all")
    public ResultVo<PageResultDto<ArticleCardDTO>> listArticles() {
        System.out.println("获取了所有文章，位置：ArticleController");
        return ResultVo.ok(articleService.listArticles());
    }

    @ApiOperation("根据分类id获取文章")
    @GetMapping("/articles/categoryId")
    public ResultVo<PageResultDto<ArticleCardDTO>> getArticlesByCategoryId(@RequestParam Integer categoryId) {
        return ResultVo.ok(articleService.listArticlesByCategoryId(categoryId));
    }

    /**
     * 前台
     *
     * @param articleId
     * @return
     */
    @ApiOperation("根据id获取文章")
    @GetMapping("/articles/{articleId}")
    public ResultVo<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        ArticleDTO articleById = articleService.getArticleById(articleId);
        return ResultVo.ok(articleById);
    }

    @ApiOperation("校验文章访问密码")
    @PostMapping("/articles/access")
    public ResultVo<?> accessArticle(@Valid @RequestBody ArticlePasswordVO articlePasswordVO) {
        articleService.accessArticle(articlePasswordVO);
        return ResultVo.ok();
    }

    @ApiOperation("根据标签id获取文章")
    @GetMapping("/articles/tagId")
    public ResultVo<PageResultDto<ArticleCardDTO>> listArticlesByTagId(@RequestParam Integer tagId) {
        return ResultVo.ok(articleService.listArticlesByTagId(tagId));
    }

    @ApiOperation("获取所有文章归档")
    @GetMapping("/archives/all")
    public ResultVo<PageResultDto<ArchiveDTO>> listArchives() {
        return ResultVo.ok(articleService.listArchives());
    }

    @ApiOperation("获取后台文章")
    @GetMapping("/admin/articles")
    public ResultVo<PageResultDto<ArticleAdminDTO>> listArticlesAdmin(ConditionVO conditionVO) {
        return ResultVo.ok(articleService.listArticlesAdmin(conditionVO));
    }

    /**
     * 获取菜谱列表
     */
    @GetMapping("/admin/articles/dinglist")
    public ResultVo<PageResultDto<AdminDingVo>> dinglist(ConditionVO conditionVO) {
        //餐厅用户
        String userRole = userAuthMapper.getUserRole(conditionVO.getUserAuthId());
        if (!userRole.equals("餐厅用户")) {
            return ResultVo.fail("只有餐厅用户可以查看");
        }
        Integer userId = userAuthMapper.getUserId(conditionVO.getUserAuthId());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getPid, userId);
        Page<Article> page = new Page<>(conditionVO.getCurrent(), conditionVO.getSize());
        Page<Article> articlePage = articleService.page(page, queryWrapper);

        List<AdminDingVo> voList = new ArrayList<>();
        articlePage.getRecords().forEach(cp -> {
            AdminDingVo build = AdminDingVo.builder()
                    .id(cp.getId())
                    .articleCover(cp.getArticleCover())
                    .articleTitle(cp.getArticleTitle())
                    .price(cp.getDiningPrice())
                    .categoryName(userAuthMapper.getCategoryName(cp.getCategoryId()))
                    .desc(cp.getCpDesc())
                    .createTime(cp.getCreateTime())
                    .build();
            voList.add(build);
        });

        return ResultVo.ok(new PageResultDto<>(voList, articleService.list().size()));
    }

    //应该就是添加文章
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation("保存和修改文章")
    @PostMapping("/admin/articles")
    public ResultVo<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO) {
        Integer userInfoId = UserUtil.getUserDetailsDTO().getUserInfoId();
        String role = articleMapper.getRoleByUserInfoId(userInfoId);
        //如果为空 即为新增 限制用户新增数量
        if (ObjectUtils.isEmpty(articleVO.getId())) {
            if (role.equals("admin")) {
                return ResultVo.fail("只能由餐厅用户新增");
            }
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getUserId, userInfoId);
            queryWrapper.isNull(Article::getPid);
            int count = articleService.count(queryWrapper);
            System.out.println("统计数据");
            System.err.println(count);
            if (count > 0) {
                return ResultVo.fail("同一个用户只能新增一个");
            }
        }

        articleService.saveOrUpdateArticle(articleVO);
        return ResultVo.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("修改文章是否置顶和推荐")
    @PutMapping("/admin/articles/topAndFeatured")
    public ResultVo<?> updateArticleTopAndFeatured(@Valid @RequestBody ArticleTopFeaturedVO articleTopFeaturedVO) {

        articleService.updateArticleTopAndFeatured(articleTopFeaturedVO);
        return ResultVo.ok();
    }

    /**
     * 后台的
     *
     * @param deleteVO
     * @return
     */
    @ApiOperation("删除或者恢复文章")
    @PutMapping("/admin/articles")
    public ResultVo<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return ResultVo.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles/delete")
    public ResultVo<?> deleteArticles(@RequestBody List<Integer> articleIds) {
        articleService.deleteArticles(articleIds);
        return ResultVo.ok();
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation("上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/articles/images")
    public ResultVo<String> saveArticleImages(MultipartFile file) {
        System.out.println("******************");
        //return ResultVo.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
        String s = qiniuUploadService.uploadImg(file);
        return ResultVo.ok(s);
    }

    @ApiOperation("根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/admin/articles/{articleId}")
    public ResultVo<ArticleAdminViewDTO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return ResultVo.ok(articleService.getArticleByIdAdmin(articleId));
    }


    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    public ResultVo<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return ResultVo.ok(articleService.listArticlesBySearch(condition));
    }

}
