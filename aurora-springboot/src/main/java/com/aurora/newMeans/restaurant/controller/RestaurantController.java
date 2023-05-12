package com.aurora.newMeans.restaurant.controller;

import com.aurora.domain.entity.Article;
import com.aurora.domain.vo.ResultVo;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.newMeans.restaurant.domain.DinIngVo;
import com.aurora.newMeans.restaurant.domain.ResAddDto;
import com.aurora.service.ArticleService;
import com.aurora.service.UserAuthService;
import com.aurora.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserAuthService userAuthService;

    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private ArticleMapper articleMapper;

    /**
     * 前台首页 点击文章 跳转后 显示所有的菜单
     * 返回的每个菜单的信息
     * articleId 餐厅的id
     */
    @GetMapping("/allList")
    public ResultVo<?> getAllList(@RequestParam(name = "articleId")Integer articleId){
        Integer userId  = articleMapper.findUserIdById(articleId);
        List<Article> articleList = articleService.list(new LambdaQueryWrapper<Article>().eq(Article::getPid,userId));
        List<DinIngVo> dinIngVos = new ArrayList<>();
        articleList.forEach(article -> {
            DinIngVo build = DinIngVo.builder()
                    .title(article.getArticleTitle())
                    .imgSrc(article.getArticleCover())
                    .price(article.getDiningPrice())
                    .desc(article.getCpDesc())
                    .build();
            dinIngVos.add(build);
        });
        return ResultVo.ok(dinIngVos);
    }

    /**
     * pid为空的表示是餐厅
     * 不为空的表示为菜谱
     * pid 放userinfo里面的主键id
     *
     * 后台 菜谱新增
     */
    @PostMapping("/addres")
    public ResultVo<?> addres(@RequestBody ResAddDto resAddDto){
        //只能是餐厅用户新增菜谱
        if (StringUtils.isEmpty(resAddDto.getUserAuthId())){
            return ResultVo.fail("用户信息为空，发生了一个错误");
        }
        String isRestaurant = userAuthMapper.getUserRole(resAddDto.getUserAuthId());
        if (!isRestaurant.equals("餐厅用户")){
            return ResultVo.fail("只有餐厅用户可以添加");
        }

        Article build = Article.builder()
                .articleTitle(resAddDto.getCpName())
                .diningPrice(resAddDto.getCpPrice())
                .cpDesc(resAddDto.getCpDesc())
                .articleContent("暂无")
                .articleCover(resAddDto.getAvatar())
                .userId(userAuthMapper.getUserId(resAddDto.getUserAuthId()))
                .pid(userAuthMapper.getUserId(resAddDto.getUserAuthId()))
                .categoryId(8)
                .build();
        articleService.save(build);
        return ResultVo.ok();
    }

    @GetMapping("/admin/getResById")
    public ResultVo<?> getResById(@RequestParam(value = "id")String id){
        System.out.println("******************");
        System.out.println(id);
        Article byId = articleService.getById(id);
        ResAddDto build = ResAddDto.builder()
                .id(byId.getId())
                .cpName(byId.getArticleTitle())
                .cpDesc(byId.getCpDesc())
                .cpPrice(String.valueOf(byId.getDiningPrice()))
                .avatar(byId.getArticleCover())
                .build();
        return ResultVo.ok(build);
    }

    @GetMapping("/typeList")
    public ResultVo<?> getTypeList(){
        System.out.println("******typeList********");
        List<String> categoryNameList = articleMapper.getTypeList();
//        Article byId = articleService.getById(id);
//        ResAddDto build = ResAddDto.builder()
//                .id(byId.getId())
//                .cpName(byId.getArticleTitle())
//                .cpDesc(byId.getCpDesc())
//                .cpPrice(String.valueOf(byId.getDiningPrice()))
//                .avatar(byId.getArticleCover())
//                .build();
        return ResultVo.ok(categoryNameList);
    }

    /**
     * 修改菜谱信息
     */
    @PostMapping("/updateRes")
    public ResultVo<?> updateRes(@RequestBody ResAddDto resAddDto){
        //只能是餐厅用户新增菜谱
//        if (StringUtils.isEmpty(resAddDto.getUserAuthId())){
//            return ResultVo.fail("用户信息为空，发生了一个错误");
//        }
//        String isRestaurant = userAuthMapper.getUserRole(resAddDto.getUserAuthId());
//        if (!isRestaurant.equals("餐厅用户")){
//            return ResultVo.fail("只有餐厅用户可以添加");
//        }
        Article byId = articleService.getById(resAddDto.getId());
        byId.setArticleCover(resAddDto.getAvatar());
        byId.setCpDesc(resAddDto.getCpDesc());
        byId.setArticleTitle(resAddDto.getCpName());
        byId.setDiningPrice(resAddDto.getCpPrice());

        articleService.updateById(byId);
        return ResultVo.ok();
    }

    /**
     * 修改菜谱信息
     */
    @GetMapping("/deleteRes")
    public ResultVo<?> deleteRes(@RequestParam(value = "id")String id){
        //只能是餐厅用户新增菜谱
        articleService.removeById(id);
        return ResultVo.ok();
    }

 }
