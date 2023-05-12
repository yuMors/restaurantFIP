package com.aurora.service.impl;

import com.aurora.domain.dto.*;
import com.aurora.domain.entity.Article;
import com.aurora.domain.entity.Comment;
import com.aurora.domain.entity.UserInfo;
import com.aurora.domain.vo.CommentVO;
import com.aurora.domain.vo.ConditionVO;
import com.aurora.domain.vo.ReviewVO;
import com.aurora.enums.CommentTypeEnum;
import com.aurora.exception.BizException;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.CommentMapper;
import com.aurora.mapper.UserInfoMapper;
import com.aurora.service.AuroraInfoService;
import com.aurora.service.CommentService;
import com.aurora.util.HTMLUtil;
import com.aurora.util.PageUtil;
import com.aurora.util.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aurora.constant.CommonConstant.FALSE;
import static com.aurora.constant.CommonConstant.TRUE;
import static com.aurora.enums.CommentTypeEnum.*;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;


    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AuroraInfoService auroraInfoService;


    private static final List<Integer> types = new ArrayList<>();

    @PostConstruct
    public void init() {
        CommentTypeEnum[] values = CommentTypeEnum.values();
        for (CommentTypeEnum value : values) {
            types.add(value.getType());
        }
    }

    /**
     * 添加评论
     */
    @Override
    public void saveComment(CommentVO commentVo) {
        this.checkCommentVo(commentVo);
        WebsiteConfigDTO websiteConfig = auroraInfoService.getWebsiteConfig();
        Integer isCommentReview = websiteConfig.getIsCommentReview();
        commentVo.setCommentContent(HTMLUtil.filter(commentVo.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtil.getUserDetailsDTO().getUserInfoId())
                .replyUserId(commentVo.getReplyUserId())
                .topicId(commentVo.getTopicId())
                .commentContent(commentVo.getCommentContent())
                .parentId(commentVo.getParentId())
                .type(commentVo.getType())
                .isReview(isCommentReview == TRUE ? FALSE : TRUE)
                .build();
        commentMapper.insert(comment);
        String fromNickname = UserUtil.getUserDetailsDTO().getNickname();
        //这个应该是邮箱通知功能 先不开发
//        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
//            this.notice(comment, fromNickname);
//        }
    }

    /**
     * 获取评论
     * 应该是获取这个文章的所有评论
     */
    @Override
    public PageResultDto<CommentDTO> listComments(CommentVO commentVo, Long current, Long size) {
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(commentVo.getTopicId()), Comment::getTopicId, commentVo.getTopicId())
                .eq(Comment::getType, commentVo.getType())
                .isNull(Comment::getParentId));
        if (commentCount == 0) {
            return new PageResultDto<>();
        }
        Long limitCurrent = PageUtil.getLimitCurrent();
        //List<CommentDTO> commentDtos = commentMapper.listComments(PageUtil.getLimitCurrent(), PageUtil.getSize(), commentVo);
        List<CommentDTO> commentDtos = commentMapper.listComments(limitCurrent, size, commentVo);
        if (CollectionUtils.isEmpty(commentDtos)) {
            return new PageResultDto<>();
        }
        List<Integer> commentIds = commentDtos.stream().map(CommentDTO::getId).collect(Collectors.toList());
        //List<ReplyDTO> replyDtos = commentMapper.listReplies(commentIds);
        List<ReplyDTO> replyDtos = this.listRepliesYH(commentIds);
        Map<Integer, List<ReplyDTO>> replyMap = replyDtos.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        commentDtos.forEach(item -> item.setReplyDTOs(replyMap.get(item.getId())));
        return new PageResultDto<>(commentDtos, commentCount);
    }

    /**
     * 这个应该是查看子评论
     * 根据评论者的id查询所有评论信息
     *
     * @param commentIds 重写的
     */
    @Override
    public List<ReplyDTO> listRepliesYH(List<Integer> commentIds) {
        List<Comment> selectList = commentMapper.selectList(new LambdaQueryWrapper<Comment>().in(Comment::getParentId, commentIds));
        //List<Comment> comments = commentMapper.selectBatchIds(commentIds);
        List<ReplyDTO> replyDtos = new ArrayList<>();
        selectList.forEach(com -> {
            ReplyDTO build = ReplyDTO.builder()
                    .userId(com.getUserId())
                    .nickname(this.getUserInfo(com.getUserId()).getNickname())
                    .avatar(this.getUserInfo(com.getUserId()).getAvatar())
                    .replyUserId(com.getReplyUserId())
                    .nickname(this.getUserInfo(com.getReplyUserId()).getNickname())
                    .id(com.getId())
                    .parentId(com.getParentId())
                    .commentContent(com.getCommentContent())
                    .createTime(com.getCreateTime()).build();
            replyDtos.add(build);
        });

        return replyDtos;
    }

    /**
     * 根据用户id得到用户的昵称
     */
    @Override
    public UserInfo getUserInfo(Integer id) {
        if (StringUtils.isBlank(String.valueOf(id))){
            return UserInfo.builder().nickname("昵称为空或其他的空").build();
        }
        return userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfo>()
                        .eq(UserInfo::getId, id));
    }


    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId) {
        //List<ReplyDTO> replyDtos = this.listRepliesYH(commentIds);
        //return commentMapper.listReplies(Collections.singletonList(commentId));
        return this.listRepliesYH(Collections.singletonList(commentId));
    }

    /**
     * 前台 进去初始化的时候加载的
     * 获取前六个评论
     */
    @Override
    public List<CommentDTO> listTopSixComments() {
        return commentMapper.listTopSixComments();
    }

    @SneakyThrows
    @Override
    public PageResultDto<CommentAdminDTO> listCommentsAdmin(ConditionVO conditionVO) {
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> commentMapper.countComments(conditionVO));
        List<CommentAdminDTO> commentBackDTOList = commentMapper.listCommentsAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        return new PageResultDto<>(commentBackDTOList, asyncCount.get());
    }

    @Override
    public void updateCommentsReview(ReviewVO reviewVO) {
        List<Comment> comments = reviewVO.getIds().stream().map(item -> Comment.builder()
                .id(item)
                .isReview(reviewVO.getIsReview())
                .build())
                .collect(Collectors.toList());
        this.updateBatchById(comments);
    }

    /**
     * 检查参数是否合规
     */
    public void checkCommentVo(CommentVO commentVO) {
        if (!types.contains(commentVO.getType())) {
            throw new BizException("参数校验异常");
        }
        if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == ARTICLE || Objects.requireNonNull(getCommentEnum(commentVO.getType())) == TALK) {
            if (Objects.isNull(commentVO.getTopicId())) {
                throw new BizException("参数校验异常");
            } else {
                if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == ARTICLE) {
                    Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId, Article::getUserId).eq(Article::getId, commentVO.getTopicId()));
                    if (Objects.isNull(article)) {
                        throw new BizException("参数校验异常");
                    }
                }
            }
        }
        if (Objects.requireNonNull(getCommentEnum(commentVO.getType())) == LINK
                || Objects.requireNonNull(getCommentEnum(commentVO.getType())) == ABOUT
                || Objects.requireNonNull(getCommentEnum(commentVO.getType())) == MESSAGE) {
            if (Objects.nonNull(commentVO.getTopicId())) {
                throw new BizException("参数校验异常");
            }
        }
        if (Objects.isNull(commentVO.getParentId())) {
            if (Objects.nonNull(commentVO.getReplyUserId())) {
                throw new BizException("参数校验异常");
            }
        }
        if (Objects.nonNull(commentVO.getParentId())) {
            Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getType).eq(Comment::getId, commentVO.getParentId()));
            if (Objects.isNull(parentComment)) {
                throw new BizException("参数校验异常");
            }
            if (Objects.nonNull(parentComment.getParentId())) {
                throw new BizException("参数校验异常");
            }
            if (!commentVO.getType().equals(parentComment.getType())) {
                throw new BizException("参数校验异常");
            }
            if (Objects.isNull(commentVO.getReplyUserId())) {
                throw new BizException("参数校验异常");
            } else {
                UserInfo existUser = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().select(UserInfo::getId).eq(UserInfo::getId, commentVO.getReplyUserId()));
                if (Objects.isNull(existUser)) {
                    throw new BizException("参数校验异常");
                }
            }
        }
    }


}
