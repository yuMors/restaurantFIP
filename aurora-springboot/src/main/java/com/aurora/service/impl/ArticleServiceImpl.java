package com.aurora.service.impl;

import com.aurora.domain.dto.*;
import com.aurora.domain.entity.Article;
import com.aurora.domain.entity.ArticleTag;
import com.aurora.domain.entity.Category;
import com.aurora.domain.entity.Tag;
import com.aurora.domain.vo.*;
import com.aurora.exception.BizException;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.ArticleTagMapper;
import com.aurora.mapper.CategoryMapper;
import com.aurora.mapper.TagMapper;
import com.aurora.service.ArticleService;
import com.aurora.service.ArticleTagService;
import com.aurora.service.RedisService;
import com.aurora.service.TagService;
import com.aurora.strategy.context.SearchStrategyContext;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.PageUtil;
import com.aurora.util.SpringUtil;
import com.aurora.util.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aurora.constant.RedisConstant.ARTICLE_ACCESS;
import static com.aurora.constant.RedisConstant.ARTICLE_VIEWS_COUNT;
import static com.aurora.enums.ArticleStatusEnum.DRAFT;
import static java.util.Objects.isNull;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private SearchStrategyContext searchStrategyContext;

    /**
     * 前台
     * 获取置顶和推荐文章
     */
//    @SneakyThrows
    @Override
    public TopAndFeaturedArticlesDTO listTopAndFeaturedArticles() {
        List<ArticleCardDTO> articleCardDTOs = articleMapper.listTopAndFeaturedArticles();
        if (articleCardDTOs.size() == 0) {
            return new TopAndFeaturedArticlesDTO();
        } else if (articleCardDTOs.size() > 3) {
            articleCardDTOs = articleCardDTOs.subList(0, 3);
        }
        TopAndFeaturedArticlesDTO topAndFeaturedArticlesDTO = new TopAndFeaturedArticlesDTO();
        topAndFeaturedArticlesDTO.setTopArticle(articleCardDTOs.get(0));
        articleCardDTOs.remove(0);
        topAndFeaturedArticlesDTO.setFeaturedArticles(articleCardDTOs);
        return topAndFeaturedArticlesDTO;
    }

    /**
     * 前台
     * 获取所有文章
     */
    @SneakyThrows
    @Override
    public PageResultDto<ArticleCardDTO> listArticles() {

        Integer asyncCount = articleMapper.selectCount(null);
        System.out.println("current: "+PageUtil.getLimitCurrent()+" size: "+PageUtil.getSize());
        List<ArticleCardDTO> articles = articleMapper.listArticles(PageUtil.getLimitCurrent(), PageUtil.getSize());
        List<ArticleCardDTO> collect = articles.stream().filter(art -> isNull( articleMapper.findPidById(art.getId()))).collect(Collectors.toList());
        return new PageResultDto<>(collect, asyncCount);
    }

    @SneakyThrows
    @Override
    public PageResultDto<ArticleCardDTO> listArticlesByCategoryId(Integer categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, categoryId);
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> articleMapper.selectCount(queryWrapper));
        List<ArticleCardDTO> articles = articleMapper.getArticlesByCategoryId(PageUtil.getLimitCurrent(), PageUtil.getSize(), categoryId);
        return new PageResultDto<>(articles, asyncCount.get());
    }

    /**
     * 根据id获取文章
     */
    @SneakyThrows
    @Override
    public ArticleDTO getArticleById(Integer articleId) {

        updateArticleViewsCount(articleId);

        CompletableFuture<ArticleDTO> asyncArticle = CompletableFuture.supplyAsync(() -> articleMapper.getArticleById(articleId));
        CompletableFuture<ArticleCardDTO> asyncPreArticle = CompletableFuture.supplyAsync(() -> {
            ArticleCardDTO preArticle = articleMapper.getPreArticleById(articleId);
            if (isNull(preArticle)) {
                preArticle = articleMapper.getLastArticle();
            }
            return preArticle;
        });
        CompletableFuture<ArticleCardDTO> asyncNextArticle = CompletableFuture.supplyAsync(() -> {
            ArticleCardDTO nextArticle = articleMapper.getNextArticleById(articleId);
            if (isNull(nextArticle)) {
                nextArticle = articleMapper.getFirstArticle();
            }
            return nextArticle;
        });
        ArticleDTO article = asyncArticle.get();
        if (isNull(article)) {
            return null;
        }
        Double score = redisService.zScore(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(score)) {
            article.setViewCount(score.intValue());
        }
        article.setPreArticleCard(asyncPreArticle.get());
        article.setNextArticleCard(asyncNextArticle.get());
        return article;
    }

    @Override
    public void accessArticle(ArticlePasswordVO articlePasswordVO) {
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articlePasswordVO.getArticleId()));
        if (isNull(article)) {
            throw new BizException("文章不存在");
        }
        if (article.getPassword().equals(articlePasswordVO.getArticlePassword())) {
            redisService.sAdd(ARTICLE_ACCESS + UserUtil.getUserDetailsDTO().getId(), articlePasswordVO.getArticleId());
        } else {
            throw new BizException("密码错误");
        }
    }

    @SneakyThrows
    @Override
    public PageResultDto<ArticleCardDTO> listArticlesByTagId(Integer tagId) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tagId);
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> articleTagMapper.selectCount(queryWrapper));
        List<ArticleCardDTO> articles = articleMapper.listArticlesByTagId(PageUtil.getLimitCurrent(), PageUtil.getSize(), tagId);
        return new PageResultDto<>(articles, asyncCount.get());
    }

    @SneakyThrows
    @Override
    public PageResultDto<ArchiveDTO> listArchives() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>().eq(Article::getIsDelete, 0).eq(Article::getStatus, 1);
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> articleMapper.selectCount(queryWrapper));
        List<ArticleCardDTO> articles = articleMapper.listArchives(PageUtil.getLimitCurrent(), PageUtil.getSize());
        HashMap<String, List<ArticleCardDTO>> map = new HashMap<>();
        for (ArticleCardDTO article : articles) {
            LocalDateTime createTime = article.getCreateTime();
            int month = createTime.getMonth().getValue();
            int year = createTime.getYear();
            String key = year + "-" + month;
            if (isNull(map.get(key))) {
                List<ArticleCardDTO> articleCardDTOS = new ArrayList<>();
                articleCardDTOS.add(article);
                map.put(key, articleCardDTOS);
            } else {
                map.get(key).add(article);
            }
        }
        List<ArchiveDTO> archiveDTOs = new ArrayList<>();
        map.forEach((key, value) -> archiveDTOs.add(ArchiveDTO.builder().Time(key).articles(value).build()));
        archiveDTOs.sort((o1, o2) -> {
            String[] o1s = o1.getTime().split("-");
            String[] o2s = o2.getTime().split("-");
            int o1Year = Integer.parseInt(o1s[0]);
            int o1Month = Integer.parseInt(o1s[1]);
            int o2Year = Integer.parseInt(o2s[0]);
            int o2Month = Integer.parseInt(o2s[1]);
            if (o1Year > o2Year) {
                return -1;
            } else if (o1Year < o2Year) {
                return 1;
            } else return Integer.compare(o2Month, o1Month);
        });
        return new PageResultDto<>(archiveDTOs, asyncCount.get());
    }

    @SneakyThrows
    @Override
    public PageResultDto<ArticleAdminDTO> listArticlesAdmin(ConditionVO conditionVO) {
//        Integer asyncCount = articleMapper.countArticleAdmins(conditionVO);
        Integer asyncCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().isNull(Article::getPid));
        List<ArticleAdminDTO> articleAdminDTOs = articleMapper.listArticlesAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);

        // 假设ArticleAdminDTO类已经定义了pid属性，而不需要调用getPid()方法获取
//        List<ArticleAdminDTO> articleDtos = articleAdminDTOs.stream()
//                .filter(ac -> {
//                    //调用ArticleMapper中实现的Join查询，一次性获取所有文章的PID信息
//                    Integer pid = articleMapper.findPidById(ac.getId());
//                    return pid == null ;
//                }).collect(Collectors.toList());
        /*
        原代码的问题在于在遍历articleAdminDTOs时，使用了forEach方法，这会导致代码的可读性下降，而且也不够优雅。
        同时，如果articleAdminDTOs的大小很大，那么forEach的性能也会变得很差。
        此外，原代码中使用的articleDtos是一个新的ArrayList对象，这可能会导致内存浪费。
        优化方案是使用Java 8的Stream API，将遍历操作映射到一个新的Stream对象中，
        然后使用filter操作过滤出符合条件的ArticleAdminDTO对象。
        最后将过滤后的ArticleAdminDTO对象收集到一个新的List对象中。这样代码可以更加优雅和高效。
        下面是优化后的代码，新注释已添加：
         */
        List<ArticleAdminDTO> articleDtos = new ArrayList<>();
        articleAdminDTOs.forEach(ac ->{
            Integer pid = articleMapper.findPidById(ac.getId());
            if (SpringUtil.isEmpty(pid)){
                articleDtos.add(ac);
            }
        });
        Map<Object, Double> viewsCountMap = redisService.zAllScore(ARTICLE_VIEWS_COUNT);
        articleDtos.forEach(item -> {
            Double viewsCount = viewsCountMap.get(item.getId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
        });
        //筛选 管理员可以看所有 餐厅用户只可以看自己
        Integer userInfoId = UserUtil.getUserDetailsDTO().getUserInfoId();
        String roleName = articleMapper.getRoleByUserInfoId(userInfoId);
//        if (roleName.equals("餐厅用户")){
//
//            articleDtos.stream().
//        }
        return new PageResultDto<>(articleDtos, asyncCount);
    }

    //文章添加
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        System.err.println("-----------------");
        System.out.println(articleVO);
        Category category = saveArticleCategory(articleVO);
        Article article = BeanCopyUtil.copyObject(articleVO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        Integer userInfoId = UserUtil.getUserDetailsDTO().getUserInfoId();

        article.setUserId(userInfoId);
        this.saveOrUpdate(article);
        saveArticleTag(articleVO, article.getId());
//        if (article.getStatus().equals(1)) {
//            rabbitTemplate.convertAndSend(SUBSCRIBE_EXCHANGE, "*", new Message(JSON.toJSONBytes(article.getId()), new MessageProperties()));
//        }
    }

    @Override
    public void updateArticleTopAndFeatured(ArticleTopFeaturedVO articleTopFeaturedVO) {
        Article article = Article.builder()
                .id(articleTopFeaturedVO.getId())
                .isTop(articleTopFeaturedVO.getIsTop())
                .isFeatured(articleTopFeaturedVO.getIsFeatured())
                .build();
        articleMapper.updateById(article);
    }

    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
//        List<Article> articles = deleteVO.getIds().stream()
//                .map(id -> Article.builder()
//                        .id(id)
//                        .isDelete(deleteVO.getIsDelete())
//                        .build())
//                .collect(Collectors.toList());
        this.removeByIds(deleteVO.getIds());
//        this.updateBatchById(articles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticles(List<Integer> articleIds) {
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIds));
        articleMapper.deleteBatchIds(articleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleAdminViewDTO getArticleByIdAdmin(Integer articleId) {
        Article article = articleMapper.selectById(articleId);
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        List<String> tagNames = tagMapper.listTagNamesByArticleId(articleId);
        ArticleAdminViewDTO articleAdminViewDTO = BeanCopyUtil.copyObject(article, ArticleAdminViewDTO.class);
        articleAdminViewDTO.setCategoryName(categoryName);
        articleAdminViewDTO.setTagNames(tagNames);
        return articleAdminViewDTO;
    }


    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    public void updateArticleViewsCount(Integer articleId) {
        redisService.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1D);
    }

    private Category saveArticleCategory(ArticleVO articleVO) {
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, articleVO.getCategoryName()));
        if (isNull(category) && !articleVO.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder()
                    .categoryName(articleVO.getCategoryName())
                    .build();
            categoryMapper.insert(category);
        }
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveArticleTag(ArticleVO articleVO, Integer articleId) {
        if (Objects.nonNull(articleVO.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        List<String> tagNames = articleVO.getTagNames();
        if (CollectionUtils.isNotEmpty(tagNames)) {
            List<Tag> existTags = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNames));
            List<String> existTagNames = existTags.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIds = existTags.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            tagNames.removeAll(existTagNames);
            if (CollectionUtils.isNotEmpty(tagNames)) {
                List<Tag> tags = tagNames.stream().map(item -> Tag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                tagService.saveBatch(tags);
                List<Integer> tagIds = tags.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                existTagIds.addAll(tagIds);
            }
            List<ArticleTag> articleTags = existTagIds.stream().map(item -> ArticleTag.builder()
                            .articleId(articleId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTags);
        }
    }

}
