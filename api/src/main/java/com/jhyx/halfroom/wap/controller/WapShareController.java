package com.jhyx.halfroom.wap.controller;

import com.jhyx.halfroom.bean.Book;
import com.jhyx.halfroom.bean.ShareInfo;
import com.jhyx.halfroom.bean.User;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.commons.MD5Util;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.constant.ShareInfoTypeStatus;
import com.jhyx.halfroom.constant.UserRoleStatus;
import com.jhyx.halfroom.service.BookService;
import com.jhyx.halfroom.service.CommonService;
import com.jhyx.halfroom.service.ShareInfoService;
import com.jhyx.halfroom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping(value = "/v4/wap/share")
@RequiredArgsConstructor
@Slf4j
public class WapShareController {
    private final BookService bookService;
    private final CommonService commonService;
    private final ShareInfoService shareInfoService;
    private final UserService userService;

    @GetMapping(value = "/home/page/image", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> homePageShareImage(@RequestParam Integer bookId,
                                             @RequestParam(required = false, defaultValue = "true") Boolean isExperience,
                                             HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            User user = userService.getUserById(userId);
            ShareInfo shareInfo = shareInfoService.getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus.WAP_HOME_PAGE, bookId, user.getRole().equals(UserRoleStatus.SALE_AMBASSADOR.getIndex()), isExperience);
            LocalDateTime createTime = LocalDateTimeUtil.parseLocalDateTime(user.getCreateTime());
            long days = LocalDateTimeUtil.differDays(createTime == null ? LocalDateTime.now() : createTime, LocalDateTime.now());
            String key = MD5Util.getMD5Code(days + shareInfo.getBgImageName() + shareInfo.getRedirectUrl() + userId + bookId + user.getName() + user.getRole()) + ".jpg";
            String url = shareInfo.getRedirectUrl() + "&bookId=" + bookId + "&user_id=" + userId;
            url = commonService.getShareImageUrl(shareInfo, key, url, user.getName(), Long.toString(days));
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), url);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/knowledge/present/image", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> knowledgePresentShareImage(@RequestParam Integer bookId) {
        try {
            Book book = bookService.getBookById(bookId);
            ShareInfo shareInfo = shareInfoService.getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus.WAP_KNOWLEDGE_PRESENT, null, null, null);
            String key = MD5Util.getMD5Code(shareInfo.getBgImageName() + shareInfo.getRedirectUrl() + bookId + new Date().getTime()) + ".jpg";
            String url = shareInfo.getRedirectUrl() + bookId;
            url = commonService.getShareImageUrl(shareInfo, key, url, book.getName());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), url);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
