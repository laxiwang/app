package com.jhyx.halfroom.api.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.commons.MD5Util;
import com.jhyx.halfroom.commons.UniqueKeyGeneratorUtil;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.service.*;
import com.jhyx.halfroom.vo.app.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v4/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final BookService bookService;
    private final BookChapterPartService bookChapterPartService;
    private final UserBookService userBookService;
    private final QiNiuService qiNiuService;
    private final UserFeedBackService userFeedBackService;
    private final UserPlayRecordService userPlayRecordService;
    private final CommonService commonService;
    private final ShareInfoService shareInfoService;
    private final BranchInviteService branchInviteService;
    private final BranchSalerService branchSalerService;
    private final UserRechargeRecordService userRechargeRecordService;
    private final UserRechargeGoodsKeyService userRechargeGoodsKeyService;
    private final UserManualService userManualService;
    @Value("${qiniu.resource_name}")
    private String resourceBucketName;

    @GetMapping(value = "/register/tips", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Map<String, Object>> registerTips(HttpServletRequest request) {
        try {
            Map<String, Object> result = null;
            Long userId = commonService.getUserIdNeedLogin(request);
            User user = userService.getUserById(userId);
            if (user != null && !StringUtils.isEmpty(user.getCreateTime())) {
                LocalDateTime createTime = LocalDateTimeUtil.parseLocalDateTime(user.getCreateTime());
                long days = LocalDateTimeUtil.differDays(createTime, LocalDateTime.now());
                if (days < 7) {
                    result = new HashMap<>();
                    result.put("createTime", LocalDateTimeUtil.formatLocalDateTime(createTime));
                    result.put("tips", "距畅游期结束还剩" + (7 - days) + "天，目前所有课程畅通无阻~ ");
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PostMapping(value = "/recharge/record", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<UserRechargeRecordVo> saveUserRechargeRecord(@RequestParam Integer rechargePoint, HttpServletRequest request) {
        try {
            UserRechargeRecordVo result = new UserRechargeRecordVo();
            Long userId = commonService.getUserIdNeedLogin(request);
            UserRechargeRecord rechargeRecord = new UserRechargeRecord();
            rechargeRecord.setRechargePoint(rechargePoint).setUserId(userId).setStatus(UserRechargeRecordStatus.NO_CHARGE.getIndex());
            rechargeRecord.setId(MD5Util.getMD5Code(UniqueKeyGeneratorUtil.generateToken() + new Date().getTime()).substring(0, 20));
            userRechargeRecordService.saveUserRechargeRecord(rechargeRecord);
            UserRechargeGoodsKey userRechargeGoodsKey = userRechargeGoodsKeyService.getUserRechargeGoodsKeyByRechargePoint(rechargePoint);
            if (userRechargeGoodsKey != null) {
                result.setKey(userRechargeGoodsKey.getKey());
            }
            result.setOrderNo(rechargeRecord.getId());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<UserInfoVo> userInfo(HttpServletRequest request) {
        try {
            User user = commonService.getUserNeedLogin(request);
            if (user == null)
                return new Result<>(ResultCode.USER_NOT_EXITS.getIndex(), ResultCode.USER_NOT_EXITS.getMsg(), null);
            UserInfoVo vo = new UserInfoVo();
            vo.setUserId(user.getId()).setName(user.getName()).setAvatar(user.getHeadImage());
            vo.setGender(user.getGender()).setGrade(user.getGrade()).setFavouriteSubject(user.getFavouriteSubject());
            if (!StringUtils.isEmpty(user.getBirthday())) {
                vo.setBirthday(LocalDateTimeUtil.formatLocalDate(LocalDateTimeUtil.parseLocalDate(user.getBirthday())));
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), vo);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PostMapping(value = "/update/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> updateUserInfo(@RequestParam(required = false) String name,
                                          @RequestParam(required = false) String gender,
                                          @RequestParam(required = false) String grade,
                                          @RequestParam(required = false) String birthday,
                                          @RequestParam(required = false) String favouriteSubject,
                                          @RequestParam(required = false) MultipartFile avatar,
                                          HttpServletRequest request) {
        try {
            User user = commonService.getUserNeedLogin(request);
            if (user == null)
                return new Result<>(ResultCode.USER_NOT_EXITS.getIndex(), ResultCode.USER_NOT_EXITS.getMsg(), null);
            String picture = null;
            if (avatar != null && avatar.getSize() > 0) {
                picture = qiNiuService.upload(avatar.getBytes(), resourceBucketName);
            }
            if (!StringUtils.isEmpty(picture))
                user.setHeadImage(picture);
            if (!StringUtils.isEmpty(name))
                user.setName(name);
            if (!StringUtils.isEmpty(gender))
                user.setGender(gender);
            if (!StringUtils.isEmpty(grade))
                user.setGrade(grade);
            if (!StringUtils.isEmpty(birthday))
                user.setBirthday(birthday);
            if (!StringUtils.isEmpty(favouriteSubject))
                user.setFavouriteSubject(favouriteSubject);
            userService.updateUser(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
    }

    @PostMapping(value = "/feed/back", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> saveUserFeedBack(@RequestParam String content, HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            if (userId == null)
                return new Result<>(ResultCode.USER_NOT_EXITS.getIndex(), ResultCode.USER_NOT_EXITS.getMsg(), null);
            String now = LocalDateTimeUtil.getCurrentTimestamp();
            userFeedBackService.saveUserFeedBack(new UserFeedBack().setUserId(userId).setContent(content).setCreateTime(now).setUpdateTime(now));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
    }

    @GetMapping(value = "/play/record/history", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @SuppressWarnings("unchecked")
    public Result<List<Map<String, Object>>> userPlayRecordHistory(HttpServletRequest request) {
        try {
            List<Map<String, Object>> results = null;
            List<UserPlayRecord> records = userPlayRecordService.getUserPlayRecordByUserId(commonService.getUserIdNeedLogin(request));
            if (records != null && records.size() > 0) {
                results = new ArrayList<>();
                Set<Integer> bookIds = records.stream().map(UserPlayRecord::getBookId).collect(Collectors.toSet());
                List<Book> books = bookService.getBookByIds(bookIds);
                List<BookChapterPart> bookChapterPartList = bookChapterPartService.getBookChapterPartListByBookIds(bookIds);
                Map<Integer, List<UserPlayRecord>> bookGroup = records.stream().collect(Collectors.groupingBy(UserPlayRecord::getBookId));
                for (Map.Entry<Integer, List<UserPlayRecord>> entry : bookGroup.entrySet()) {
                    Map<String, Object> map = new HashMap<>();
                    Integer bookId = entry.getKey();
                    List<UserPlayRecord> list = entry.getValue();
                    Book book = books.stream().filter(bk -> bk.getId().equals(bookId)).findFirst().orElseGet(Book::new);
                    if (book.getId() != null) {
                        map.put("bookName", book.getName());
                        map.put("bookFaceThumbUrl", book.getFaceThumbUrl());
                    }
                    list.sort((r1, r2) -> -(r1.getUpdateTime().compareToIgnoreCase(r2.getUpdateTime())));
                    long days = LocalDateTimeUtil.differDays(LocalDateTimeUtil.parseLocalDateTime(list.get(0).getUpdateTime()), LocalDateTime.now());
                    if (days <= 3) {
                        map.put("type", 0);
                    } else if (days <= 7) {
                        map.put("type", 1);
                    } else if (days <= 30) {
                        map.put("type", 2);
                    } else {
                        map.put("type", 3);
                    }
                    List<Map<String, Object>> playDetailsList = new ArrayList<>();
                    for (UserPlayRecord playRecord : list) {
                        BookChapterPart bookChapterPart = bookChapterPartList.stream().filter(bcp -> bcp.getBookId().equals(playRecord.getBookId()) && bcp.getBookChapterId().equals(playRecord.getChapterId()) && bcp.getBookChapterPartId().equals(playRecord.getPartId())).findFirst().orElseGet(BookChapterPart::new);
                        if (bookChapterPart.getId() != null) {
                            Map<String, Object> playDetails = new HashMap<>();
                            playDetails.put("bookId", playRecord.getBookId());
                            playDetails.put("bookChapterId", playRecord.getChapterId());
                            playDetails.put("bookChapterPartId", playRecord.getPartId());
                            playDetails.put("partFaceThumbUrl", bookChapterPart.getAppUrl());
                            playDetails.put("partName", bookChapterPart.getName());
                            playDetails.put("endTime", playRecord.getPlayTimeEnd() == null ? 0 : playRecord.getPlayTimeEnd());
                            playDetails.put("sumTime", bookChapterPart.getTimeSum());
                            playDetails.put("updateTime", playRecord.getUpdateTime());
                            playDetails.put("video", bookChapterPart.getType().equals(BookChapterPartTypeStatus.VIDEO.getIndex()));
                            playDetailsList.add(playDetails);
                        }
                    }
                    map.put("playDetails", playDetailsList);
                    results.add(map);
                }
                results.sort((m1, m2) -> -(((String) ((List<Map<String, Object>>) m1.get("playDetails")).get(0).get("updateTime")).compareTo(((String) ((List<Map<String, Object>>) m2.get("playDetails")).get(0).get("updateTime")))));
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/purchase/book", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<UserPurchaseBookVo>> userPurchaseBookList(HttpServletRequest request) {
        List<UserPurchaseBookVo> results = new ArrayList<>();
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            List<UserBook> list = userBookService.getUserPurchasedBookList(userId);
            for (UserBook userBook : list) {
                UserPurchaseBookVo vo = new UserPurchaseBookVo();
                Book book = bookService.getBookById(userBook.getBookId());
                vo.setBookId(book.getId()).setFaceThumbUrl(book.getSmallFaceThumbUrl()).setGrade(book.getGrade()).setName(book.getName()).setSpeaker(book.getSpeaker()).setSectionCount(book.getCourseCount()).setType(UserPurchaseBookStatus.PAY.getIndex());
                results.add(vo);
            }
            List<Manual> manualList = userManualService.getManualListByUserId(userId);
            if (manualList != null && manualList.size() > 0) {
                for (Manual manual : manualList) {
                    UserPurchaseBookVo vo = new UserPurchaseBookVo();
                    vo.setUrl(manual.getUrl()).setName(manual.getName()).setFaceThumbUrl(manual.getFaceThumbUrl()).setSpeaker(manual.getSpeaker()).setPage(manual.getPage()).setType(UserPurchaseBookStatus.PRESENTATION.getIndex());
                    results.add(vo);
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/share/image", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<String>> userShareImage(HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            User user = userService.getUserById(userId);
            ShareInfo shareInfo;
            if (user.getRole().equals(UserRoleStatus.SPREAD_AMBASSADOR.getIndex()))
                shareInfo = shareInfoService.getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus.APP_SPREAD_AMBASSADOR, null, null, null);
            else
                shareInfo = shareInfoService.getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus.APP_SALE_AMBASSADOR, null, null, null);
            LocalDateTime createTime = LocalDateTimeUtil.parseLocalDateTime(user.getCreateTime());
            long days = LocalDateTimeUtil.differDays(createTime == null ? LocalDateTime.now() : createTime, LocalDateTime.now());
            String url = shareInfo.getRedirectUrl() + "&user_id=" + userId;
            String key = MD5Util.getMD5Code(shareInfo.getBgImageName() + url + userId + user.getName() + days) + ".jpg";
            String url1 = commonService.getShareImageUrl(shareInfo, key, url, user.getName(), Long.toString(days));
            String url2 = commonService.getUserStudyPunchTimeCardImageUrl(userId);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), List.of(url1, url2));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/invite/message", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<UserMessageVo> userInviteMessage(HttpServletRequest request) {
        UserMessageVo result = null;
        try {
            BranchInvite source = null;
            Long userId = commonService.getUserIdNeedLogin(request);
            List<BranchInvite> list = branchInviteService.getBranchInviteListByUserIdAndStatus(userId, BranchInviteStatus.WAIT_INVITE);
            if (list != null && list.size() > 0) {
                source = list.get(0);
            }
            if (source != null) {
                Integer saleId = source.getBranchsalerId();
                BranchSaler branchSaler = branchSalerService.getBranchSalerById(saleId);
                if (branchSaler != null) {
                    result = new UserMessageVo();
                    result.setId(source.getId()).setBranchId(branchSaler.getId()).setBranchName(branchSaler.getFullname()).setUserId(userId).setMessage("您好，" + branchSaler.getFullname() + "分会邀请您成为销售大使");
                    result.setInviteTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(source.getCreatetime())));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @GetMapping(value = "/invite/message/user/read/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> userInviteMessageReadStatus(HttpServletRequest request) {
        Boolean unRead = Boolean.FALSE;
        Long userId = commonService.getUserIdNeedLogin(request);
        List<BranchInvite> list = branchInviteService.getBranchInviteListByUserIdAndStatus(userId, BranchInviteStatus.WAIT_INVITE);
        if (list != null && list.size() > 0) {
            unRead = list.get(0).getUserReadStatus().equals(UserReadBranchInviteStatus.UN_READ.getIndex());
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), unRead);
    }

    @PutMapping(value = "/update/invite/message/user/read/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> updateUserInviteMessageReadStatus(@RequestParam Integer id) {
        BranchInvite branchInvite = branchInviteService.getBranchInviteById(id);
        if (branchInvite == null)
            return new Result<>(ResultCode.BRANCH_INVITE_RECORD_NOT_EXITS.getIndex(), ResultCode.BRANCH_INVITE_RECORD_NOT_EXITS.getMsg(), null);
        branchInvite.setUserReadStatus(UserReadBranchInviteStatus.READ.getIndex());
        branchInviteService.updateBranchInvite(branchInvite);
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
    }

    @GetMapping(value = "/study/punch/time", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<StudyPunchTimeVo> userStudyPunchTime(HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            StudyPunchTimeVo result = commonService.getUserStudyPunchTime(userId);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
