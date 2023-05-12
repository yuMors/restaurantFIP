package com.aurora.controller.blog;

import com.aurora.domain.dto.blogDto.DiningHallDto;
import com.aurora.domain.entity.Article;
import com.aurora.domain.vo.ResultVo;
import com.aurora.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/dining")
public class DiningController {
    @Autowired
    private ArticleService articleService;

    /**
     * 后台吧
     * 查询餐厅菜单 菜谱 信息
     */
    @GetMapping("/listDining")
    public ResultVo<?> listDining(@RequestParam(name = "superiorId") Integer superiorId) {
//        List<Article> articleList = articleService.list(
//                new LambdaQueryWrapper<Article>()
//                        .eq(Article::getSuperiorId, superiorId)
//        );
        System.err.println("调用了修改superiorId的地方 先改为pid 位置 DiningController");
        List<Article> articleList = articleService.list();
        List<DiningHallDto> diningDtos = new ArrayList<>();
        articleList.forEach(article -> {
            DiningHallDto build = DiningHallDto.builder()
                    .img(article.getArticleCover())
                    .title(article.getArticleTitle())
                    .price(article.getDiningPrice())
                    .desc(article.getArticleContent())
                    .build();
            diningDtos.add(build);
        });
        return ResultVo.ok(diningDtos);
    }
}
