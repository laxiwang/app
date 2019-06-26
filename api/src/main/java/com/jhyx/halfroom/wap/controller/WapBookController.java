package com.jhyx.halfroom.wap.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.FigureUtil;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.service.*;
import com.jhyx.halfroom.serviceImpl.QiNiuServiceImpl;
import com.jhyx.halfroom.vo.app.UserPlayRecordCountVo;
import com.jhyx.halfroom.vo.common.CommonBookChapterVo;
import com.jhyx.halfroom.vo.common.KnowledgeContentVo;
import com.jhyx.halfroom.vo.wap.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/v4/wap/book")
@RequiredArgsConstructor
@Slf4j
public class WapBookController {
    private final BookService bookService;
    private final CommonService commonService;
    private final BookChapterPartService bookChapterPartService;
    private final UserPlayRecordService userPlayRecordService;
    private final QiNiuService qiNiuService;
    private final BookChapterService bookChapterService;
    private final BookChapterPartContentService bookChapterPartContentService;
    private final CardService cardService;
    private final UserAnswerInfoService userAnswerInfoService;
    @Value("${qiniu.vip_bucket_name}")
    private String vipBucketName;

    @GetMapping(value = "/list/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BookVo>> bookInfoList() {
        try {
            List<BookVo> results = new ArrayList<>();
            List<Book> books = bookService.getAllBookList();
            books.forEach(book -> results.add(new BookVo().setCategory(book.getCategory()).setBookId(book.getId()).setName(book.getName()).setFaceThumbUrl(book.getSmallFaceThumbUrl()).setAgeMax(book.getAgeMax()).setAgeMin(book.getAgeMin()).setCourseCount(book.getCourseCount()).setPrice(book.getPrice()).setSpeaker(book.getSpeaker()).setBrief(book.getSpeakerBrief())));
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/receive/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BookReceiveVo>> receiveBookInfo(@RequestParam(required = false) Integer cardNo,
                                                       HttpServletRequest request) {
        List<BookReceiveVo> results = new ArrayList<>();
        try {
            List<Book> list = bookService.getAllBookList();
            if (cardNo != null) {
                Card card = cardService.getCardByCardNo(cardNo);
                if (card != null && list != null && list.size() > 0) {
                    list = list.stream().filter(book -> book.getPrice().equals(card.getFee())).collect(Collectors.toList());
                }
            }
            if (list != null && list.size() > 0) {
                list.forEach(book -> {
                    BookReceiveVo vo = new BookReceiveVo();
                    vo.setBookId(book.getId()).setFaceThumbUrl(book.getSmallFaceThumbUrl()).setName(book.getName());
                    vo.setGrade(book.getGrade()).setPrice(book.getPrice()).setSpeaker(book.getSpeaker());
                    vo.setCourseCount(book.getCourseCount()).setStatus(commonService.userToBookStatus(book.getId(), request));
                    results.add(vo);
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
    }

    @GetMapping(value = "/purchase/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookPurchaseDetailVo> bookPurchaseDetail(@RequestParam Integer bookId) {
        try {
            BookPurchaseDetailVo result = new BookPurchaseDetailVo();
            Book book = bookService.getBookById(bookId);
            result.setBookId(bookId).setName(book.getName()).setPrice(book.getPrice());
            result.setFaceThumbUrl(book.getSmallFaceThumbUrl());
            result.setCourseCount(book.getCourseCount());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookInfoVo> bookInfo(@RequestParam Integer bookId, HttpServletRequest request) {
        BookInfoVo bookInfo = new BookInfoVo();
        try {
            Long userId = commonService.getUserIdNoNeedLogin(request);
            Book book = bookService.getBookById(bookId);
            bookInfo.setPurchaseNotes(book.getPurchaseNotes()).setName(book.getName()).setFaceThumbUrl(book.getFaceThumbUrl());
            bookInfo.setSpeakerBriefUrl(book.getSpeakerBriefUrl()).setOutlineUrl(book.getOutlineUrl()).setBriefUrl(book.getBriefUrl());
            bookInfo.setPrice(book.getPrice()).setStatus(commonService.userToBookStatus(bookId, request)).setCourseCount(book.getCourseCount());
            List<BookInfoBookChapterPartVo> medias = new ArrayList<>();
            List<BookChapterPart> list = bookChapterPartService.bookChapterPartFreeList(bookId, 0, Integer.MAX_VALUE);
            if (list != null && list.size() > 0) {
                list.forEach(bcp -> {
                    BookInfoBookChapterPartVo vo = new BookInfoBookChapterPartVo();
                    vo.setBookId(bookId).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId()).setAnswerInfo(userAnswerInfoService.userAnswerInfo(userId, bookId, bcp.getBookChapterId(), bcp.getBookChapterPartId()));
                    vo.setName(bcp.getName()).setDuration(bcp.getTimeSum()).setFree(Boolean.TRUE).setPermission(Boolean.TRUE).setVideo(bcp.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                    vo.setPlaySum(userPlayRecordService.getBookChapterPartPlayRecordCount(bookId, bcp.getBookChapterId(), bcp.getBookChapterPartId()));
                    medias.add(vo);
                });
            }
            list = bookChapterPartService.getBookChapterPartListByBookIdAndChapterIdAndState(bookId, null, BookChapterPartStatus.CHARGE.getIndex(), 0, 4);
            List<UserAnswerInfo> userAnswerInfoList = userAnswerInfoService.userAnswerInfoList(userId, bookId, null, null);
            if (list != null && list.size() > 0) {
                Boolean permission = commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request);
                list.forEach(bcp -> {
                    BookInfoBookChapterPartVo vo = new BookInfoBookChapterPartVo();
                    vo.setName(bcp.getName()).setDuration(bcp.getTimeSum()).setFree(Boolean.FALSE).setPermission(permission).setVideo(bcp.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                    vo.setPlaySum(userPlayRecordService.getBookChapterPartPlayRecordCount(bookId, bcp.getBookChapterId(), bcp.getBookChapterPartId()));
                    vo.setBookId(bookId).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId()).setAnswerInfo("未答题");
                    for (UserAnswerInfo userAnswerInfo : userAnswerInfoList) {
                        if (userAnswerInfo.getBookChapterId().equals(bcp.getBookChapterId()) && userAnswerInfo.getBookChapterPartId().equals(bcp.getBookChapterPartId())) {
                            vo.setAnswerInfo("答对" + userAnswerInfo.getCorrectCount() + "/3");
                            break;
                        }
                    }
                    medias.add(vo);
                });
            }
            bookInfo.setMedias(medias);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), bookInfo);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookResourceVo> bookList(@RequestParam Integer bookId,
                                           @RequestParam(required = false) Integer bookChapterId,
                                           HttpServletRequest request) {
        BookResourceVo result = new BookResourceVo();
        try {
            List<BookChapterPartVo> freeModule = new ArrayList<>();
            List<BookChapterVo> chapterModule = new ArrayList<>();
            Book book = bookService.getBookById(bookId);
            List<CommonBookChapterVo> bookChapterInfo = new ArrayList<>();
            List<BookChapter> bookChapters = bookChapterService.getBookChapterListByBookId(bookId);
            bookChapters.sort(Comparator.comparing(BookChapter::getBookChapterId));
            if (bookChapterId == null) {
                bookChapterId = bookChapters.get(0).getBookChapterId();
            }
            Boolean permission = commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request);
            List<UserPlayRecordCountVo> collections = userPlayRecordService.getUserPlayRecordListByBookIdAndBookChapterId(bookId, null);
            List<BookChapterPart> list = bookChapterPartService.getBookChapterPartListByBookId(bookId);
            List<BookChapterPart> freeList = list.stream().filter(bcp -> bcp.getState().equals(BookChapterPartStatus.FREE.getIndex())).collect(Collectors.toList());
            for (BookChapterPart bcp : freeList) {
                BookChapterPartVo vo = new BookChapterPartVo();
                vo.setName(bcp.getName()).setDuration(bcp.getTimeSum()).setFree(Boolean.TRUE).setVideo(bcp.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                vo.setBookId(bcp.getBookId()).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId()).setPermission(Boolean.TRUE);
                Long playSum = 0L;
                for (var model : collections) {
                    if (model.getBookId().equals(bcp.getBookId()) && model.getBookChapterId().equals(bcp.getBookChapterId()) && model.getBookChapterPartId().equals(bcp.getBookChapterPartId())) {
                        playSum += model.getPlayCount();
                    }
                }
                vo.setPlaySum(playSum);
                freeModule.add(vo);
            }
            result.setFreeModule(freeModule);
            final Integer partId = bookChapterId;
            list.removeIf(bcp -> !(bcp.getBookChapterId().equals(partId)));
            BookChapterVo vo = new BookChapterVo();
            List<BookChapterPartVo> parts = new ArrayList<>();
            vo.setBookChapterId(bookChapterId);
            Optional<BookChapter> optional = bookChapters.stream().filter(bc -> bc.getBookChapterId().equals(partId)).findFirst();
            optional.ifPresent(bc -> {
                String str = "第" + FigureUtil.Number2Chinese(partId) + "单元 " + bc.getName();
                vo.setChapterName(str);
            });
            list.forEach(bcp -> {
                BookChapterPartVo bp = new BookChapterPartVo();
                bp.setFree(bcp.getState().equals(BookChapterPartStatus.FREE.getIndex())).setDuration(bcp.getTimeSum()).setName(bcp.getName());
                bp.setBookId(bcp.getBookId()).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId());
                bp.setVideo(bcp.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex())).setPermission(bp.isFree());
                Long playSum = 0L;
                for (var model : collections) {
                    if (model.getBookId().equals(bcp.getBookId()) && model.getBookChapterPartId().equals(bcp.getBookChapterPartId()) && model.getBookChapterId().equals(bcp.getBookChapterId())) {
                        playSum += model.getPlayCount();
                    }
                }
                bp.setPlaySum(playSum);
                if (!bp.isPermission()) {
                    bp.setPermission(permission);
                }
                parts.add(bp);
            });
            vo.setParts(parts);
            chapterModule.add(vo);
            result.setChapterModule(chapterModule);
            result.setStatus(commonService.userToBookStatus(bookId, request));
            result.setCourseCount(book.getCourseCount());
            result.setUpdateCourseCount(book.getUpdateCourseCount());
            for (BookChapter bc : bookChapters) {
                CommonBookChapterVo cb = new CommonBookChapterVo();
                cb.setBookChapterId(bc.getBookChapterId()).setName("第" + FigureUtil.Number2Chinese(bc.getBookChapterId()) + "单元").setTitle(bc.getName());
                bookChapterInfo.add(cb);
            }
            result.setBookChapterInfo(bookChapterInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), result);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @GetMapping(value = "/course/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<CourseDetailVo> detail(@RequestParam Integer bookId,
                                         @RequestParam Integer bookChapterId,
                                         @RequestParam Integer bookChapterPartId,
                                         @RequestParam(required = false, defaultValue = "false") Boolean experience,
                                         HttpServletRequest request) {
        CourseDetailVo result = new CourseDetailVo();
        try {
            Long userId = commonService.getUserIdNoNeedLogin(request);
            BookChapterPart resource = bookChapterPartService.getBookChapterPartByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            if (resource == null)
                return new Result<>(ResultCode.MEDIA_NOT_EXITS.getIndex(), ResultCode.MEDIA_NOT_EXITS.getMsg(), null);
            KnowledgeContentVo vo = commonService.getKnowledgeContentByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            String playUrl;
            if (experience) {
                playUrl = resource.getExperienceUrl();
            } else {
                playUrl = commonService.getBookChapterPartPlayUrl(resource, request);
            }
            result.setBookChapterPartName(resource.getName()).setBookId(resource.getBookId()).setBookChapterId(resource.getBookChapterId());
            result.setBackgroundImageUrl(resource.getImgUrl()).setVideo(BookChapterPartTypeStatus.VIDEO.getIndex().equals(resource.getType()));
            result.setDuration(resource.getTimeSum()).setPlayUrl(playUrl).setBookChapterPartId(resource.getBookChapterPartId()).setImageUrl(vo.getImageUrl());
            result.setCourseTestList(vo.getCourseTestList());
            commonService.saveOrUpdateUserPlayRecord(bookId, bookChapterId, bookChapterPartId, request);
            result.setAnswerStatus(commonService.getUserAnswerInfoStatus(userId, bookId, bookChapterId, bookChapterPartId));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @GetMapping(value = "/course/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BookChapterPartDetailVo>> coursePlayList(@RequestParam Integer bookId,
                                                                HttpServletRequest request) {
        try {
            List<BookChapterPartDetailVo> result = new ArrayList<>();
            Long userId = commonService.getUserIdNoNeedLogin(request);
            List<BookChapterPart> list = bookChapterPartService.getBookChapterPartListByBookIdAndChapterIdAndType(bookId, null, null, null, null);
            if (!(commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request)))
                list = list.stream().filter(bcp -> bcp.getState().equals(BookChapterPartStatus.FREE.getIndex())).collect(Collectors.toList());
            if (list != null && list.size() > 0)
                list.forEach(bcp -> result.add(new BookChapterPartDetailVo().setFree(BookChapterPartStatus.FREE.getIndex().equals(bcp.getState())).setVideo(BookChapterPartTypeStatus.VIDEO.getIndex().equals(bcp.getType())).setDuration(bcp.getTimeSum()).setName(bcp.getName()).setBookId(bcp.getBookId()).setBookChapterId(bcp.getBookChapterId()).setBookChapterPartId(bcp.getBookChapterPartId())));
            for (BookChapterPartDetailVo vo : result) {
                Long count = userPlayRecordService.getBookChapterPartPlayRecordCount(vo.getBookId(), vo.getBookChapterId(), vo.getBookChapterPartId());
                vo.setPlaySum(count).setPermission(Boolean.TRUE).setAnswerInfo("未答题");
            }
            List<UserAnswerInfo> userAnswerInfoList = userAnswerInfoService.userAnswerInfoList(userId, bookId, null, null);
            for (BookChapterPartDetailVo vo : result) {
                for (UserAnswerInfo userAnswerInfo : userAnswerInfoList) {
                    if (userAnswerInfo.getBookChapterId().equals(vo.getBookChapterId()) && userAnswerInfo.getBookChapterPartId().equals(vo.getBookChapterPartId())) {
                        vo.setAnswerInfo("答对" + userAnswerInfo.getCorrectCount() + "/3");
                        break;
                    }
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/content", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<BookChapterPartContentVo> bookChapterPartContent(@RequestParam Integer bookId,
                                                                   @RequestParam Integer bookChapterId,
                                                                   @RequestParam Integer bookChapterPartId) {
        try {
            BookChapterPartContent bookContent = bookChapterPartContentService.getBookChapterPartContentByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            if (bookContent == null)
                return new Result<>(ResultCode.BOOK_CHAPTER_PART_CONTENT_NOT_EXITS.getIndex(), ResultCode.BOOK_CHAPTER_PART_CONTENT_NOT_EXITS.getMsg(), null);
            BookChapterPart bookChapterPart = bookChapterPartService.getBookChapterPartByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            BookChapterPartContentVo result = new BookChapterPartContentVo();
            QiNiuServiceImpl.Bucket bucket = qiNiuService.getBucket(vipBucketName);
            result.setAudioUrl(bucket.getDownloadUrl(bookChapterPart.getVipUrl())).setImageUrl(bookChapterPart.getImgUrl());
            result.setContent(bookContent.getContent()).setSubTitle(bookContent.getSubTitle()).setTitle(bookContent.getTitle());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
