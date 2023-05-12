package com.aurora.controller;

import com.aurora.annotation.AccessLimit;
import com.aurora.annotation.OptLog;
import com.aurora.domain.dto.CommentAdminDTO;
import com.aurora.domain.dto.CommentDTO;
import com.aurora.domain.dto.PageResultDto;
import com.aurora.domain.dto.ReplyDTO;
import com.aurora.domain.vo.CommentVO;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.ResultVo;
import com.aurora.domain.vo.ReviewVO;
import com.aurora.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @AccessLimit(seconds = 60, maxCount = 3)
    @ApiOperation("添加评论")
    @PostMapping("/comments/save")
    public ResultVo<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return ResultVo.ok();
    }

    @ApiOperation("获取评论")
    @GetMapping("/comments")
    public ResultVo<PageResultDto<CommentDTO>> getComments(CommentVO commentVo,
                                                           @RequestParam(name = "current") Long current,
                                                           @RequestParam(name = "size") Long size) {
        return ResultVo.ok(commentService.listComments(commentVo,current,size));
    }

    /**
     * 根据commentId获取回复
     */
    @ApiOperation(value = "根据commentId获取回复")
    @GetMapping("/comments/{commentId}/replies")
    public ResultVo<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return ResultVo.ok(commentService.listRepliesByCommentId(commentId));
    }

    /**
     * 前台 进去初始化的时候加载的
     * 获取前六个评论
     */
    @ApiOperation("获取前六个评论")
    @GetMapping("/comments/topSix")
    public ResultVo<List<CommentDTO>> listTopSixComments() {
        return ResultVo.ok(commentService.listTopSixComments());
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    public ResultVo<PageResultDto<CommentAdminDTO>> listCommentBackDTO(ConditionVO conditionVO) {
        return ResultVo.ok(commentService.listCommentsAdmin(conditionVO));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    public ResultVo<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return ResultVo.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    public ResultVo<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return ResultVo.ok();
    }

}
