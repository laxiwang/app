package com.jhyx.halfroom.common.controller;

import com.jhyx.halfroom.bean.BookChapterPartComment;
import com.jhyx.halfroom.bean.BookChapterPartCommentPraise;
import com.jhyx.halfroom.bean.User;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.BookChapterPartCommentPraiseService;
import com.jhyx.halfroom.service.BookChapterPartCommentService;
import com.jhyx.halfroom.service.CommonService;
import com.jhyx.halfroom.service.UserService;
import com.jhyx.halfroom.vo.common.CommentVo;
import com.jhyx.halfroom.vo.common.CreateCommentResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v4/common/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final BookChapterPartCommentService commentService;
    private final BookChapterPartCommentPraiseService bookChapterPartCommentPraiseService;
    private final CommonService commonService;
    private final UserService userService;

    @GetMapping(value = "/hot/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<CommentVo>> hotCommentList(@RequestParam Long bookId,
                                                  @RequestParam(required = false) Integer offset,
                                                  @RequestParam(required = false) Integer limit,
                                                  HttpServletRequest request) {
        try {
            List<BookChapterPartComment> list = commentService.getCommentsByHotComment(bookId, offset, limit);
            List<CommentVo> results = bookChapterPartCommentList2bookChapterPartCommentVo(list, request);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/normal/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<CommentVo>> commentList(@RequestParam Long bookId,
                                               @RequestParam Integer chapterId,
                                               @RequestParam Integer partId,
                                               @RequestParam(required = false) Integer offset,
                                               @RequestParam(required = false) Integer limit,
                                               HttpServletRequest request) {
        try {
            List<BookChapterPartComment> comments = new ArrayList<>();
            List<BookChapterPartComment> praiseComments = commentService.getCommentIdsByPraise(bookId, chapterId, partId, null, null);
            praiseComments.removeIf(bcp -> bcp.getAtCommentId() != null);
            if ((offset == null && limit == null)) {
                comments = praiseComments;
            }
            if (offset != null && limit != null && offset < praiseComments.size()) {
                comments = praiseComments.subList(offset, praiseComments.size() - offset < limit ? praiseComments.size() : limit + offset);
            }
            if ((offset == null && limit == null) || (offset != null && limit != null && limit > comments.size())) {
                List<BookChapterPartComment> noPraiseList;
                if (limit == null) {
                    noPraiseList = commentService.getCommentsByNotPraise(bookId, chapterId, partId, null, null);
                } else {
                    noPraiseList = commentService.getCommentsByNotPraise(bookId, chapterId, partId, offset - praiseComments.size() <= 0 ? 0 : offset - praiseComments.size(), limit - comments.size());
                }
                if (noPraiseList != null && noPraiseList.size() > 0) {
                    comments.addAll(noPraiseList);
                }
            }
            Set<Long> ids = comments.stream().map(BookChapterPartComment::getId).collect(Collectors.toSet());
            if (ids.size() > 0) {
                List<BookChapterPartComment> list = commentService.getCommentsByAtCommentIds(ids);
                comments.addAll(list);
            }
            List<CommentVo> results = bookChapterPartCommentList2bookChapterPartCommentVo(comments, request);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<CreateCommentResultVo> createComment(BookChapterPartComment comment, HttpServletRequest request) {
        CreateCommentResultVo result = new CreateCommentResultVo();
        result.setFlag(Boolean.FALSE);
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            log.info("创建评论,获取userId为：{}", userId);
            comment.setUserId(userId);
            log.info("开始创建评论，评论信息为：{}", comment);
            Integer row = commentService.saveBookChapterPartComment(comment);
            if (row == 1) {
                result.setFlag(Boolean.TRUE);
                result.setCommentId(comment.getId());
                return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), result);
    }

    @PutMapping(value = "/praise", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> praiseComment(@RequestParam Long commentId,
                                         @RequestParam Boolean praise,
                                         HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            log.info("进入点赞api，用户id为：{}", userId);
            if (praise) {
                BookChapterPartCommentPraise commentPraise = bookChapterPartCommentPraiseService.getBookChapterPartCommentPraiseByCommentIdAndUserId(commentId, userId);
                if (commentPraise == null) {
                    commentPraise = new BookChapterPartCommentPraise();
                    commentPraise.setUserId(userId).setBookChapterPartCommentId(commentId);
                    bookChapterPartCommentPraiseService.saveBookChapterPartCommentPraise(commentPraise);
                    log.info("点赞成功");
                }
            } else {
                bookChapterPartCommentPraiseService.deleteBookChapterPartCommentPraiseByCommentIdAndUserId(commentId, userId);
                log.info("取消点赞成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
    }

    private List<CommentVo> bookChapterPartCommentList2bookChapterPartCommentVo(List<BookChapterPartComment> comments, HttpServletRequest request) {
        List<CommentVo> results = new ArrayList<>();
        if (comments.size() > 0) {
            List<User> userList = userService.getUserListByIds(comments.stream().map(BookChapterPartComment::getUserId).collect(Collectors.toSet()));
            comments.forEach(bcc -> {
                CommentVo vo = new CommentVo();
                vo.setId(bcc.getId()).setType(bcc.getType()).setTimeSum(bcc.getTimeSum()).setAtCommentId(bcc.getAtCommentId()).setCommentMsg(bcc.getContent()).setCreateTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(bcc.getCreateTime())));
                Optional<User> optional = userList.stream().filter(u -> u.getId().equals(bcc.getUserId())).findFirst();
                optional.ifPresent(user -> vo.setAvatar(user.getHeadImage()).setNickName(user.getName()));
                vo.setPraiseCount(bookChapterPartCommentPraiseService.getBookChapterPartCommentPraiseCount(bcc.getId()));
                vo.setPraise(Boolean.FALSE);
                Long userId = commonService.getUserIdNoNeedLogin(request);
                if (userId != null) {
                    BookChapterPartCommentPraise praise = bookChapterPartCommentPraiseService.getBookChapterPartCommentPraiseByCommentIdAndUserId(bcc.getId(), userId);
                    vo.setPraise(praise != null);
                }
                results.add(vo);
            });
        }
        return results;
    }
}
