package com.jhyx.halfroom.api.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.FigureUtil;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.service.*;
import com.jhyx.halfroom.vo.app.*;
import com.jhyx.halfroom.vo.common.CommonBookChapterVo;
import com.jhyx.halfroom.vo.common.KnowledgeContentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v4/api/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookChapterPartService bookChapterPartService;
    private final BookChapterService bookChapterService;
    private final BookService bookService;
    private final QiNiuService qiNiuService;
    private final UserBookService userBookService;
    private final UserService userService;
    private final UserPlayRecordService userPlayRecordService;
    private final CommonService commonService;
    private final BookChapterPartContentService bookChapterPartContentService;
    private final BookChapterPartCommentService commentService;
    private final UserAnswerInfoService userAnswerInfoService;
    @Value("${qiniu.resource_name}")
    private String resourceBucketName;
    @Value("${qiniu.vip_bucket_name}")
    private String vipBucketName;

    @GetMapping(value = "/chapter/part/free/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BookChapterPartVo>> BookChapterPartFreeList(@RequestParam Integer offset, @RequestParam Integer limit) {
        List<BookChapterPartVo> results = new ArrayList<>();
        try {
            List<BookChapterPart> list = bookChapterPartService.bookChapterPartFreeList(null, offset, limit);
            if (list != null && list.size() > 0) {
                list.forEach(bcp -> {
                    BookChapterPartVo vo = new BookChapterPartVo();
                    vo.setDuration(bcp.getTimeSum()).setName(bcp.getName()).setBookId(bcp.getBookId());
                    vo.setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId());
                    vo.setVideo(bcp.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                    vo.setFaceThumbUrl(bookService.getBookById(bcp.getBookId()).getAppFaceThumbUrl());
                    results.add(vo);
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BookVo>> BookList(@RequestParam Integer category,
                                         @RequestParam Integer offset,
                                         @RequestParam(required = false, defaultValue = "2") Integer limit,
                                         HttpServletRequest request) {
        List<BookVo> results = new ArrayList<>();
        try {
            List<Book> list = bookService.getBookListByCategory(category, offset, limit);
            if (list != null && list.size() > 0) {
                for (Book book : list) {
                    BookVo vo = new BookVo();
                    vo.setBookId(book.getId()).setName(book.getName()).setSpeaker(book.getSpeaker()).setBrief(book.getBrief());
                    vo.setSpeakerBrief(book.getSpeakerBrief()).setFaceThumbUrl(book.getAppFaceThumbUrl());
                    vo.setAgeMax(book.getAgeMax()).setAgeMin(book.getAgeMin()).setMoney(book.getPrice());
                    vo.setBookChapterPartCount(book.getCourseCount()).setPurchase(commonService.judgeUserPurchaseBookNoNeedLogin(book.getId(), request));
                    vo.setPlaySum(userPlayRecordService.getBookChapterPartPlayRecordCount(book.getId(), null, null));
                    results.add(vo);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
    }

    @GetMapping(value = "/presentation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookPresentationVo> bookPresentation(@RequestParam Integer bookId, HttpServletRequest request) {
        BookPresentationVo result = new BookPresentationVo();
        try {
            Book book = bookService.getBookById(bookId);
            result.setCourseFaceThumbUrl(book.getAppFaceThumbUrl()).setPrice(book.getPrice()).setSpeaker(book.getSpeaker());
            result.setFaceThumbUrl(book.getFaceThumbUrl()).setBriefUrl(book.getBriefUrl()).setSectionCount(book.getCourseCount());
            result.setOutLineUrl(book.getOutlineUrl()).setName(book.getName()).setSpeakerBriefUrl(book.getSpeakerBriefUrl());
            List<BookChapterPart> list = bookChapterPartService.bookChapterPartFreeList(book.getId(), 0, 3);
            List<BookPresentationChapterPartVo> partVoList = new ArrayList<>(list.size());
            for (BookChapterPart vo : list) {
                BookPresentationChapterPartVo bpo = new BookPresentationChapterPartVo();
                bpo.setBookId(vo.getBookId()).setBookChapterId(vo.getBookChapterId()).setBookChapterPartId(vo.getBookChapterPartId());
                bpo.setName(vo.getName()).setTotalDuration(vo.getTimeSum()).setVideo(vo.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                partVoList.add(bpo);
            }
            result.setList(partVoList).setPurchase(commonService.judgeUserPurchaseBookNoNeedLogin(bookId, request)).setPurchaseNotes(book.getPurchaseNotes());
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @GetMapping(value = "/course", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookCourseVo> bookCourse(@RequestParam Integer bookId,
                                           @RequestParam(required = false) Integer bookChapterId,
                                           HttpServletRequest request) {
        BookCourseVo result = new BookCourseVo();
        try {
            List<CommonBookChapterVo> bookChapterInfo = new ArrayList<>();
            Book book = bookService.getBookById(bookId);
            List<BookChapter> bookChapterList = bookChapterService.getBookChapterListByBookId(bookId);
            List<BookChapterPart> chapterPartList = bookChapterPartService.getBookChapterPartListByBookId(bookId);
            Long userId = commonService.getUserIdNoNeedLogin(request);
            boolean purchase = commonService.judgeUserPurchaseBookNoNeedLogin(bookId, request);
            boolean permission = commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request);
            bookChapterList.sort(Comparator.comparing(BookChapter::getBookChapterId));
            if (bookChapterId == null) {
                bookChapterId = bookChapterList.get(0).getBookChapterId();
            }
            final Integer partId = bookChapterId;
            List<UserPlayRecordCountVo> collections = userPlayRecordService.getUserPlayRecordListByBookIdAndBookChapterId(bookId, null);
            result.setPurchase(purchase).setUpdateCount(book.getUpdateCourseCount()).setSectionCount(book.getCourseCount());
            if (!purchase && !permission) {
                List<BookChapterPart> freeList = chapterPartList.stream().filter(bcp -> bcp.getState().equals(BookChapterPartStatus.FREE.getIndex())).collect(Collectors.toList());
                List<FreeBookChapterPartVo> freeModule = new ArrayList<>();
                for (BookChapterPart bookChapterPart : freeList) {
                    FreeBookChapterPartVo vo = new FreeBookChapterPartVo();
                    vo.setName(bookChapterPart.getName()).setTotalDuration(bookChapterPart.getTimeSum()).setVideo(bookChapterPart.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                    vo.setBookId(bookChapterPart.getBookId()).setBookChapterId(bookChapterPart.getBookChapterId()).setBookChapterPartId(bookChapterPart.getBookChapterPartId()).setPlayTimeEnd(0);
                    vo.setAnswerInfo(userAnswerInfoService.userAnswerInfo(userId, bookId, bookChapterPart.getBookChapterId(), bookChapterPart.getBookChapterPartId()));
                    Long playSum = 0L;
                    for (var model : collections) {
                        if (model.getBookId().equals(bookChapterPart.getBookId()) && model.getBookChapterId().equals(bookChapterPart.getBookChapterId()) && model.getBookChapterPartId().equals(bookChapterPart.getBookChapterPartId())) {
                            playSum += model.getPlayCount();
                            if (userId != null && userId.equals(model.getUserId())) {
                                vo.setPlayTimeEnd(model.getPlayTimeEnd() != null ? model.getPlayTimeEnd() : 0);
                            }
                        }
                    }
                    vo.setPlaySum(playSum);
                    freeModule.add(vo);
                }
                result.setFreeModule(freeModule);
            }
            List<UserAnswerInfo> userAnswerInfoList = userAnswerInfoService.userAnswerInfoList(userId, bookId, bookChapterId, null);
            chapterPartList.removeIf(bcp -> !(bcp.getBookChapterId().equals(partId)));
            List<BookChapterVo> chapterModule = new ArrayList<>();
            Optional<BookChapter> op = bookChapterList.stream().filter(bc -> bc.getBookChapterId().equals(partId)).findFirst();
            BookChapter bookChapter = op.orElseGet(BookChapter::new);
            BookChapterVo vo = new BookChapterVo();
            vo.setName("第" + FigureUtil.Number2Chinese(bookChapterId) + "单元");
            vo.setTitle(bookChapter.getName());
            List<BookChapterPartCourseVo> list = new ArrayList<>();
            for (BookChapterPart bcp : chapterPartList) {
                BookChapterPartCourseVo co = new BookChapterPartCourseVo();
                co.setName(bcp.getName()).setTotalDuration(bcp.getTimeSum()).setAnswerInfo("未答题");
                co.setVideo(bcp.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex())).setFree(Boolean.FALSE).setPlayTimeEnd(0);
                co.setBookId(bcp.getBookId()).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId());
                for (UserAnswerInfo userAnswerInfo : userAnswerInfoList) {
                    if (userAnswerInfo.getBookId().equals(bcp.getBookId()) && userAnswerInfo.getBookChapterId().equals(bcp.getBookChapterId()) && userAnswerInfo.getBookChapterPartId().equals(bcp.getBookChapterPartId())) {
                        co.setAnswerInfo("答对" + userAnswerInfo.getCorrectCount() + "/3");
                        break;
                    }
                }
                Long playSum = 0L;
                for (var model : collections) {
                    if (model.getBookChapterId().equals(bcp.getBookChapterId()) && model.getBookChapterPartId().equals(bcp.getBookChapterPartId()) && model.getBookId().equals(bcp.getBookId())) {
                        playSum += model.getPlayCount();
                        if (userId != null && userId.equals(model.getUserId())) {
                            co.setPlayTimeEnd(model.getPlayTimeEnd() == null ? 0 : model.getPlayTimeEnd());
                        }
                    }
                }
                co.setPlaySum(playSum);
                if (BookChapterPartStatus.FREE.getIndex().equals(bcp.getState())) {
                    co.setPermission(Boolean.TRUE);
                } else {
                    co.setPermission(permission);
                }
                if (!purchase)
                    co.setFree(BookChapterPartStatus.FREE.getIndex().equals(bcp.getState()));
                list.add(co);
            }
            vo.setList(list);
            chapterModule.add(vo);
            result.setChapterModule(chapterModule);
            for (BookChapter bc : bookChapterList) {
                CommonBookChapterVo cb = new CommonBookChapterVo();
                cb.setTitle(bc.getName()).setBookChapterId(bc.getBookChapterId());
                cb.setName("第" + FigureUtil.Number2Chinese(bc.getBookChapterId()) + "单元");
                bookChapterInfo.add(cb);
            }
            result.setBookChapterInfo(bookChapterInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @GetMapping(value = "/course/play/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<CoursePlayDetailVo> coursePlayDetail(@RequestParam Integer bookId,
                                                       @RequestParam Integer bookChapterId,
                                                       @RequestParam Integer bookChapterPartId,
                                                       HttpServletRequest request) {
        CoursePlayDetailVo coursePlayDetailVo = new CoursePlayDetailVo();
        try {
            BookChapterPart target = bookChapterPartService.getBookChapterPartByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            if (target == null)
                return new Result<>(ResultCode.MEDIA_NOT_EXITS.getIndex(), ResultCode.MEDIA_NOT_EXITS.getMsg(), null);
            String playUrl = commonService.getBookChapterPartPlayUrl(target, request);
            if (StringUtils.isEmpty(playUrl))
                return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), null);
            commonService.saveOrUpdateUserPlayRecord(bookId, bookChapterId, bookChapterPartId, request);
            coursePlayDetailVo.setBookChapterPartName(target.getName()).setTimeSum(target.getTimeSum());
            coursePlayDetailVo.setBookId(target.getBookId()).setBookChapterId(target.getBookChapterId()).setBookChapterPartId(target.getBookChapterPartId());
            coursePlayDetailVo.setPlayUrl(playUrl).setBackgroundImageUrl(target.getImgUrl()).setId(target.getId());
            Book book = bookService.getBookById(target.getBookId());
            coursePlayDetailVo.setBookChapterPartShareBrief(book.getBookChapterPartShareBrief()).setBookName(book.getName()).setFaceThumbUrl(book.getSmallFaceThumbUrl());
            coursePlayDetailVo.setSpeaker(book.getSpeaker()).setPurchase(commonService.judgeUserPurchaseBookNoNeedLogin(bookId, request)).setTimeEnd(0);
            Long userId = commonService.getUserIdNoNeedLogin(request);
            if (userId != null) {
                UserPlayRecord record = userPlayRecordService.getUserPlayRecordByUserIdAndBookIdAndChapterIdAndPartId(userId, bookId, bookChapterId, bookChapterPartId);
                if (record != null && record.getPlayTimeEnd() != null)
                    coursePlayDetailVo.setTimeEnd(record.getPlayTimeEnd());
            }
            KnowledgeContentVo vo = commonService.getKnowledgeContentByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            coursePlayDetailVo.setCourseTestList(vo.getCourseTestList()).setAnswerStatus(commonService.getUserAnswerInfoStatus(userId, bookId, bookChapterId, bookChapterPartId));
            coursePlayDetailVo.setCommentCount(commentService.getCommentCountByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId));
            coursePlayDetailVo.setImageUrl(vo.getImageUrl()).setBookChapterPartShareBriefTitle(book.getName() + " " + target.getName() + "|" + book.getBookChapterPartShareBriefTitle());
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), coursePlayDetailVo);
    }

    @GetMapping(value = "/chapter/part/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<PlayDetailBookChapterPartListVo>> coursePlayDetail(@RequestParam Integer bookId,
                                                                          @RequestParam Boolean isVideo,
                                                                          HttpServletRequest request) {
        try {
            List<PlayDetailBookChapterPartListVo> result = new ArrayList<>();
            List<BookChapterPart> list = bookChapterPartService.getBookChapterPartListByBookIdAndChapterIdAndType(bookId, null, isVideo ? BookChapterPartTypeStatus.VIDEO.getIndex() : BookChapterPartTypeStatus.AUDIO.getIndex(), null, null);
            boolean flag = commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request);
            if (!flag)
                list = list.stream().filter(bcp -> bcp.getState().equals(BookChapterPartStatus.FREE.getIndex())).collect(Collectors.toList());
            if (list != null && list.size() > 0)
                list.forEach(bcp -> result.add(new PlayDetailBookChapterPartListVo().setTotalDuration(bcp.getTimeSum()).setName(bcp.getName()).setBookId(bcp.getBookId()).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId()).setVideo(isVideo)));
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/part/draft/flag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> partDraftFlag(@RequestParam Integer bookId,
                                         @RequestParam Integer bookChapterId,
                                         @RequestParam Integer bookChapterPartId) {
        BookChapterPartContent content;
        try {
            content = bookChapterPartContentService.getBookChapterPartContentByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), content != null);
    }

    @GetMapping(value = "/course/download/url", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> courseDownloadUrl(@RequestParam Integer bookId,
                                            @RequestParam Integer bookChapterId,
                                            @RequestParam Integer bookChapterPartId,
                                            HttpServletRequest request) {
        Long userId = commonService.getUserIdNoNeedLogin(request);
        if (userId == null)
            return new Result<>(ResultCode.USER_NOT_LOGIN.getIndex(), ResultCode.USER_NOT_LOGIN.getMsg(), null);
        UserBook userBook = userBookService.getUserBookListByUserIdAndBookId(userId, bookId);
        if (userBook == null || !userBook.getType().equals(UserBookStatus.VIP.getIndex()))
            return new Result<>(ResultCode.NO_PERMISSIONS.getIndex(), ResultCode.NO_PERMISSIONS.getMsg(), null);
        BookChapterPart resource = bookChapterPartService.getBookChapterPartByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
        if (resource == null)
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), qiNiuService.getBucket(vipBucketName).getDownloadUrl(resource.getVipUrl()));
    }

    @GetMapping(value = "/share/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookShareVo> bookShareInfo(@RequestParam Integer bookId, HttpServletRequest request) {
        try {
            BookShareVo result = new BookShareVo();
            Long userId = commonService.getUserIdNeedLogin(request);
            Book book = bookService.getBookById(bookId);
            result.setContent(book.getBrief()).setSaleAmbassador(Boolean.FALSE).setUserId(userId).setImgUrl(book.getSmallFaceThumbUrl()).setTitle(book.getBriefTitle());
            User user = userService.getUserById(userId);
            if (user != null && user.getRole() != null && user.getRole().equals(UserRoleStatus.SALE_AMBASSADOR.getIndex()))
                result.setSaleAmbassador(Boolean.TRUE);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/presentation/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookPresentationInfoVo> bookPresentationInfo(@RequestParam Integer bookId,
                                                               @RequestParam Integer bookChapterId,
                                                               @RequestParam Integer bookChapterPartId,
                                                               HttpServletRequest request) {
        BookPresentationInfoVo result = new BookPresentationInfoVo();
        try {
            Book book = bookService.getBookById(bookId);
            BookChapterPart bookChapterPart = bookChapterPartService.getBookChapterPartByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            if (bookChapterPart != null) {
                boolean permissions = bookChapterPart.getState().equals(BookChapterPartStatus.FREE.getIndex());
                if (!permissions) {
                    permissions = commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request);
                }
                result.setPermissions(permissions);
            }
            if (book != null) {
                result.setName(book.getName()).setCourseCount(book.getCourseCount()).setFaceThumbUrl(book.getAppFaceThumbUrl()).setPrice(book.getPrice()).setPurchaseNotes(book.getPurchaseNotes());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }
}
