package com.jhyx.halfroom.common.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.constant.BookChapterPartStatus;
import com.jhyx.halfroom.constant.IntegrationTypeStatus;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v4/common")
@RequiredArgsConstructor
@Slf4j
public class CommonUploadController {
    private final UserPlayRecordService userPlayRecordService;
    private final QiNiuService qiNiuService;
    private final UserPlayTimeSumService userPlayTimeSumService;
    private final CommonService commonService;
    private final BookChapterPartService bookChapterPartService;
    private final UserAnswerInfoService userAnswerInfoService;
    private final IntegralService integralService;
    @Value("${qiniu.resource_name}")
    private String resourceBucketName;

    @PutMapping(value = "/book/upload/play/end", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> uploadBookPlayTime(@RequestParam Integer bookId,
                                              @RequestParam Integer bookChapterId,
                                              @RequestParam Integer bookChapterPartId,
                                              @RequestParam Integer timeEnd,
                                              HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            UserPlayRecord record = userPlayRecordService.getUserPlayRecordByUserIdAndBookIdAndChapterIdAndPartId(userId, bookId, bookChapterId, bookChapterPartId);
            record.setPlayTimeEnd(timeEnd);
            userPlayRecordService.updateUserPlayRecord(record);
            List<UserPlayTimeSum> list = userPlayTimeSumService.getUserPlayTimeSumByUserIdAndBookIdAndBookChapterIdAndBookChapterPartId(userId, bookId, bookChapterId, bookChapterPartId);
            if (list.size() == 0) {
                UserPlayTimeSum userPlayTimeSum = new UserPlayTimeSum();
                userPlayTimeSum.setUserId(userId).setBookId(bookId).setBookChapterId(bookChapterId).setBookChapterPartId(bookChapterPartId).setPlaySum(timeEnd.longValue());
                userPlayTimeSumService.saveUserPlayTimeSum(userPlayTimeSum);
            } else {
                UserPlayTimeSum userPlayTimeSum = list.get(0);
                userPlayTimeSum.setPlaySum(userPlayTimeSum.getPlaySum() == null ? timeEnd : timeEnd + userPlayTimeSum.getPlaySum());
                userPlayTimeSumService.updateUserPlayTimeSum(userPlayTimeSum);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
    }

    @PostMapping(value = "/upload/file", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> upload(@RequestParam MultipartFile file) {
        try {
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), qiNiuService.upload(file.getBytes(), resourceBucketName));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PutMapping(value = "/upload/answer/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> uploadAnswerInfo(@RequestParam Integer bookId,
                                            @RequestParam Integer bookChapterId,
                                            @RequestParam Integer bookChapterPartId,
                                            @RequestParam Integer correctCount,
                                            HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            BookChapterPart bookChapterPart = bookChapterPartService.getBookChapterPartByBookIdAndChapterIdAndPartId(bookId, bookChapterId, bookChapterPartId);
            if (bookChapterPart == null) {
                return new Result<>(ResultCode.MEDIA_NOT_EXITS.getIndex(), ResultCode.MEDIA_NOT_EXITS.getMsg(), null);
            }
            if (correctCount == null || correctCount > 3 || correctCount < 0) {
                return new Result<>(ResultCode.ARGS_ERROR.getIndex(), ResultCode.ARGS_ERROR.getMsg(), null);
            }
            //判断是否具有答题权限
            boolean permission;
            if (bookChapterPart.getState() != null && bookChapterPart.getState().equals(BookChapterPartStatus.FREE.getIndex())) {
                permission = true;
            } else {
                permission = commonService.judgeUserPermissionWatchVipBookChapterPart(bookId, request);
            }
            if (permission) {
                List<UserAnswerInfo> list = userAnswerInfoService.userAnswerInfoList(userId, bookId, bookChapterId, bookChapterPartId);
                if (list != null && list.size() > 0) {
                    UserAnswerInfo userAnswerInfo = list.get(0);
                    if (userAnswerInfo.getCorrectCount() < correctCount) {
                        userAnswerInfo.setCorrectCount(correctCount);
                        userAnswerInfoService.updateUserAnswerInfo(userAnswerInfo);
                    }
                } else {
                    UserAnswerInfo userAnswerInfo = new UserAnswerInfo();
                    userAnswerInfo.setUserId(userId).setBookId(bookId).setBookChapterId(bookChapterId).setBookChapterPartId(bookChapterPartId).setCorrectCount(correctCount);
                    userAnswerInfoService.saveUserAnswerInfo(userAnswerInfo);
                    //执行加积分操作
                    if (correctCount > 0) {
                        IntegralOrigin integralOrigin = new IntegralOrigin();
                        integralOrigin.setIntegral(correctCount * IntegrationTypeStatus.ANSWER_CORRECT.getIntegration()).setInUserId(userId).setUserId(userId).setType(IntegrationTypeStatus.ANSWER_CORRECT.getIndex());
                        integralService.saveIntegral(integralOrigin);
                    }
                }
                return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
            } else {
                return new Result<>(ResultCode.NO_PERMISSIONS.getIndex(), ResultCode.NO_PERMISSIONS.getMsg(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
