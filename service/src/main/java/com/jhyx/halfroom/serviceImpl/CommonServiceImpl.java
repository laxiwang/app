package com.jhyx.halfroom.serviceImpl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.*;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.message.DistributionMessageBean;
import com.jhyx.halfroom.service.*;
import com.jhyx.halfroom.vo.app.StudyPunchTimeVo;
import com.jhyx.halfroom.vo.app.UserPlayTimeSumVo;
import com.jhyx.halfroom.vo.common.CourseTestVo;
import com.jhyx.halfroom.vo.common.KnowledgeContentVo;
import com.jhyx.halfroom.wechat.WeChatPayResult;
import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderParams;
import com.qiniu.storage.model.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {
    private final ProvinceCityBranchMappingService provinceCityBranchMappingService;
    private final UserBranchSaleChangeService userBranchSaleChangeService;
    private final UserService userService;
    private final UserLoginService userLoginService;
    private final UserOrderService userOrderService;
    private final PayMessageService payMessageService;
    private final UserBookService userBookService;
    private final QiNiuService qiNiuService;
    private final BookChapterPartArchitectureService bookChapterPartArchitectureService;
    private final UserPlayRecordService userPlayRecordService;
    private final BookService bookService;
    private final IntegralService integralService;
    private final BranchSalerService branchSalerService;
    private final UserConsumeRecordService userConsumeRecordService;
    private final UserRechargeRecordService userRechargeRecordService;
    private final ShareInfoService shareInfoService;
    private final UserPlayTimeSumService userPlayTimeSumService;
    private final UserAnswerInfoService userAnswerInfoService;
    @Value("${yunpian.encoding}")
    private String encoding;
    @Value("${yunpian.apiKey}")
    private String apiKey;
    @Value("${yunpian.sendCodeUrl}")
    private String sendCodeUrl;
    @Value("${qiniu.vip_bucket_name}")
    private String vipBucketName;
    @Value("${qiniu.resource_name}")
    private String resourceBucketName;
    @Value("${user.defaultAvatar}")
    private String defaultAvatar;
    @Value("${alibaba.sms.regionId}")
    private String regionId;
    @Value("${alibaba.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${alibaba.sms.secret}")
    private String secret;
    @Value("${alibaba.sms.domain}")
    private String domain;
    @Value("${alibaba.sms.version}")
    private String version;
    @Value("${alibaba.sms.action}")
    private String action;

    @Override
    public Integer getUserAnswerInfoStatus(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        List<UserAnswerInfo> userAnswerInfoList = userAnswerInfoService.userAnswerInfoList(userId, bookId, bookChapterId, bookChapterPartId);
        if (userAnswerInfoList != null && userAnswerInfoList.size() == 1) {
            if (userAnswerInfoList.get(0).getCorrectCount() == 3) {
                return AnswerInfoStatus.ALL_ANSWER_CORRECT.getIndex();
            } else {
                return AnswerInfoStatus.SECTION_ANSWER_CORRECT.getIndex();
            }
        } else {
            return AnswerInfoStatus.NO_ANSWER.getIndex();
        }
    }

    @Override
    public void sendPayNotice(UserOrder userOrder, User user, Book book) throws UnsupportedEncodingException {
        if (userOrder != null && user != null && book != null) {
            Long ambassadorId = userOrder.getIntroducerid();
            if (ambassadorId != null) {
                User ambassador = userService.getUserById(ambassadorId);
                if (ambassador != null && ambassador.getRole() != null) {
                    Long integral = 0L;
                    Long count = userService.getAmbassadorSpreadUserCount(ambassadorId);
                    List<IntegralOrigin> integralOriginList = integralService.getIntegralListByUserId(ambassadorId);
                    for (IntegralOrigin integralOrigin : integralOriginList) {
                        integral += integralOrigin.getIntegral();
                    }
                    String timestamp = LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(userOrder.getUpdatetime()));
                    String date = LocalDateTimeUtil.formatLocalDate(LocalDateTimeUtil.parseLocalDate(userOrder.getUpdatetime()));
                    sendAmbassadorNotice(ambassador.getRole().equals(UserRoleStatus.SALE_AMBASSADOR.getIndex()), true, ambassador.getPhone(), user.getName(), user.getPhone(), timestamp, book.getName(), date, count, integral);
                }
            }
        }
    }

    @Override
    public void sendMessageCode(String phone, String code) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        boolean flag = sendAliMessage(phone, AliMessageStatus.VERIFICATION_CODE.getIndex(), JSONUtil.toJSONString(map));
        if (!flag) {
            phone = URLEncoder.encode(phone, encoding);
            String value = URLEncoder.encode("#code#", encoding) + "=" + URLEncoder.encode(code, encoding);
            sendNoticeRequest(PhoneMessageStatus.VERIFICATION_CODE.getIndex().toString(), value, phone);
        }
    }

    @Override
    public void updateKnowledgeGiftUserOrder(UserOrder userOrder, String phone) {
        if (StringUtils.isNotEmpty(phone) && userOrder != null) {
            String args = "phone" + phone;
            userOrder.setOrderno(userOrder.getOrderno().substring(0, userOrder.getOrderno().length() - args.length()) + args).setPayrole(UserOrderPayRoleStatus.KNOWLEDGE_GIFT_ROLE.getIndex());
            userOrderService.updateUserOrder(userOrder);
        }
    }

    @Override
    public boolean judgeUserPurchaseBookByUserIdOrPhone(String phone, Long userId, Integer bookId) {
        boolean purchase = false;
        if (StringUtils.isNotEmpty(phone)) {
            User user = userService.getUserByPhone(phone);
            if (user != null) {
                if (judgeUserWhetherPurchaseBookByUserIdAndBookId(user.getId(), bookId)) {
                    purchase = true;
                }
            }
        } else {
            if (judgeUserWhetherPurchaseBookByUserIdAndBookId(userId, bookId)) {
                purchase = true;
            }
        }
        return purchase;
    }

    @Override
    public StudyPunchTimeVo getUserStudyPunchTime(@NotNull Long userId) {
        StudyPunchTimeVo result = new StudyPunchTimeVo();
        List<Book> books = bookService.getAllBookList();
        User user = userService.getUserById(userId);
        Long count = userService.getUserCount();
        List<UserPlayTimeSum> list = userPlayTimeSumService.getUserPlayTimeSumByUserIdAndBookIdAndBookChapterIdAndBookChapterPartId(userId, null, null, null);
        List<UserPlayRecord> records = userPlayRecordService.getUserPlayRecordByUserId(userId);
        LocalDateTime createTime = LocalDateTimeUtil.parseLocalDateTime(user.getCreateTime());
        long days = LocalDateTimeUtil.differDays(createTime == null ? LocalDateTime.now() : createTime, LocalDateTime.now());
        String avatar = user.getHeadImage();
        String name = user.getName();
        String percentage = "100%";
        long subjectEnlightenTime = 0L;
        long greatChineseTime = 0L;
        long updateClassroomTime = 0L;
        if (list.size() > 0) {
            Map<Integer, List<UserPlayTimeSum>> map = list.stream().collect(Collectors.groupingBy(UserPlayTimeSum::getBookId));
            for (Map.Entry<Integer, List<UserPlayTimeSum>> entry : map.entrySet()) {
                Integer bookId = entry.getKey();
                Integer category = books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElseGet(Book::new).getCategory();
                List<UserPlayTimeSum> li = entry.getValue();
                long time = (li.stream().map(UserPlayTimeSum::getPlaySum).mapToLong(Long::longValue).sum()) / 60;
                if (BookCategoryStatus.SUBJECT_ENLIGHTEN.getIndex().equals(category))
                    subjectEnlightenTime += time;
                else if (BookCategoryStatus.GREAT_CHINESE.getIndex().equals(category))
                    greatChineseTime += time;
                else if (BookCategoryStatus.UPDATE_CLASSROOM.getIndex().equals(category))
                    updateClassroomTime += time;
            }
        }
        long totalStudyTime = subjectEnlightenTime + greatChineseTime + updateClassroomTime;
        List<UserPlayTimeSumVo> voList = userPlayTimeSumService.getUserPlayTimeSum();
        long index = -2;
        if (voList.size() > 0) {
            index = -1;
            for (int i = 0; i < voList.size(); i++) {
                if (voList.get(i).getUserId().equals(userId)) {
                    index = i;
                    break;
                }
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        if (index >= 0) {
            double value = (double) (count - index) / (double) count;
            String str = decimalFormat.format(value);
            value = Double.parseDouble(str) * 100;
            percentage = (int) value + "%";
        }
        if (index == -1) {
            String str = decimalFormat.format((double) (count - voList.size()) / (double) count);
            double value = Double.parseDouble(str) * 100;
            percentage = (int) value + "%";
        }
        result.setUserId(userId).setName(name).setAvatar(avatar).setStudyDays(days).setStudyMinutes(totalStudyTime);
        result.setStudyParts(records.size()).setPercentage(percentage).setSubjectEnlightenMinutes(subjectEnlightenTime);
        result.setGreatChineseMinutes(greatChineseTime).setUpdateClassroomMinutes(updateClassroomTime);
        return result;
    }

    @Override
    public String getUserStudyPunchTimeCardImageUrl(@NotNull Long userId) {
        StudyPunchTimeVo vo = getUserStudyPunchTime(userId);
        ShareInfo shareInfo = shareInfoService.getShareInfoByTypeAndBookIdAndIsSaleAmbassadorAndIsExperience(ShareInfoTypeStatus.STUDY_PUNCH_TIME_CARD, null, null, null);
        String key = MD5Util.getMD5Code(vo.getAvatar() + vo.getName() + vo.getStudyDays() + vo.getStudyMinutes() + vo.getStudyParts() + vo.getSubjectEnlightenMinutes() + vo.getGreatChineseMinutes() + vo.getUpdateClassroomMinutes() + vo.getPercentage() + shareInfo.getBgImageName() + shareInfo.getRedirectUrl()) + ".jpg";
        final String PREFIX_PATH = File.separator + "opt" + File.separator + "classroom" + File.separator + "image" + File.separator;
        FileInfo fileInfo = qiNiuService.getFileInfoByKey(key, resourceBucketName);
        if (fileInfo != null)
            return qiNiuService.getBucket(resourceBucketName).getDownloadUrl(key);
        String bgImagePath = PREFIX_PATH + "bg" + File.separator;
        if (mkdirs(bgImagePath))
            bgImagePath += MD5Util.getMD5Code(shareInfo.getBgImageName());
        else
            throw new RuntimeException();
        if (!new File(bgImagePath).exists())
            qiNiuService.downloadFile(shareInfo.getBgImageName(), bgImagePath);
        String codePath = PREFIX_PATH + "code" + File.separator;
        if (mkdirs(codePath))
            codePath += MD5Util.getMD5Code(shareInfo.getRedirectUrl() + "&user_id=" + userId) + ".jpg";
        else
            throw new RuntimeException();
        if (!new File(codePath).exists())
            QRCodeUtil.encode(shareInfo.getRedirectUrl() + "&user_id=" + userId, codePath);
        String resultQrCode = PREFIX_PATH + "result_qr_code" + File.separator;
        if (mkdirs(resultQrCode))
            resultQrCode += key;
        else
            throw new RuntimeException();
        ImageUtil.composeImage(bgImagePath, codePath, resultQrCode, shareInfo.getQrX(), shareInfo.getQrY(), shareInfo.getQrW(), shareInfo.getQrH());
        String resultName = PREFIX_PATH + File.separator + "result_name" + File.separator;
        if (mkdirs(resultName))
            resultName += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultQrCode, resultName, vo.getName(), shareInfo.getFontStyle(), shareInfo.getFontSize(), shareInfo.getFontX(), shareInfo.getFontY(), shareInfo.getFontColorR(), shareInfo.getFontColorG(), shareInfo.getFontColorB());
        String avatarPath = PREFIX_PATH + "avatar" + File.separator;
        if (mkdirs(avatarPath))
            avatarPath += MD5Util.getMD5Code(vo.getAvatar());
        else
            throw new RuntimeException();
        if (!new File(avatarPath).exists())
            qiNiuService.downloadFile(vo.getAvatar(), avatarPath);
        String resultAvatar = PREFIX_PATH + "result_avatar" + File.separator;
        if (mkdirs(resultAvatar))
            resultAvatar += key;
        else
            throw new RuntimeException();
        ImageUtil.composeImage(resultName, avatarPath, resultAvatar, 68, 277, 163, 163);
        String resultContent = PREFIX_PATH + "result_content" + File.separator;
        if (mkdirs(resultContent))
            resultContent += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultAvatar, resultContent, vo.getName(), "微软雅黑", 48, 277, 336, 255, 255, 255);
        String resultDays = PREFIX_PATH + "days" + File.separator;
        if (mkdirs(resultDays))
            resultDays += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultContent, resultDays, Long.toString(vo.getStudyDays()), "微软雅黑", 70, 135, 725, 255, 255, 255);
        String resultTotalStudyTime = PREFIX_PATH + "result_total_study_time" + File.separator;
        if (mkdirs(resultTotalStudyTime))
            resultTotalStudyTime += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultDays, resultTotalStudyTime, Long.toString(vo.getStudyMinutes()), "微软雅黑", 70, 449, 725, 255, 255, 255);
        String resultPart = PREFIX_PATH + "result_part" + File.separator;
        if (mkdirs(resultPart))
            resultPart += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultTotalStudyTime, resultPart, Integer.toString(vo.getStudyParts()), "微软雅黑", 70, 780, 725, 255, 255, 255);
        String resultSubjectEnlightenTime = PREFIX_PATH + "result_subject_enlighten_time" + File.separator;
        if (mkdirs(resultSubjectEnlightenTime))
            resultSubjectEnlightenTime += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultPart, resultSubjectEnlightenTime, "学科启蒙" + vo.getSubjectEnlightenMinutes() + "分钟", "微软雅黑", 30, 136, 935, 252, 252, 252);
        String resultGreatChineseTime = PREFIX_PATH + "result_great_chinese_time" + File.separator;
        if (mkdirs(resultGreatChineseTime))
            resultGreatChineseTime += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultSubjectEnlightenTime, resultGreatChineseTime, "大语文" + vo.getGreatChineseMinutes() + "分钟", "微软雅黑", 30, 464, 935, 252, 252, 252);
        String resultUpdateClassroomTime = PREFIX_PATH + "result_update_classroom_time" + File.separator;
        if (mkdirs(resultUpdateClassroomTime))
            resultUpdateClassroomTime += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultGreatChineseTime, resultUpdateClassroomTime, "升级课堂" + vo.getUpdateClassroomMinutes() + "分钟", "微软雅黑", 30, 770, 935, 252, 252, 252);
        String resultPercentage = PREFIX_PATH + "result_percentage" + File.separator;
        if (mkdirs(resultPercentage))
            resultPercentage += key;
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(resultUpdateClassroomTime, resultPercentage, "学习能力超过了" + vo.getPercentage() + "的同学", "微软雅黑", 47, 265, 1121, 255, 255, 255);
        return qiNiuService.upload(resultPercentage, key, resourceBucketName);
    }

    @Override
    public String getUserRegisterTipsImageUrl(String experienceTime) {
        final String PREFIX_PATH = File.separator + "opt" + File.separator + "classroom" + File.separator + "image";
        final String BG_IMAGE_URL = "http://resource.half-room.com/197733CC5E7B338B.png";
        String bgImagePath = PREFIX_PATH + File.separator + "bg" + File.separator;
        if (mkdirs(bgImagePath))
            bgImagePath = bgImagePath + UUID.randomUUID().toString() + ".png";
        else
            throw new RuntimeException();
        if (!new File(bgImagePath).exists())
            qiNiuService.downloadFile(BG_IMAGE_URL, bgImagePath);
        String results = PREFIX_PATH + File.separator + "results" + File.separator;
        if (mkdirs(results))
            results = results + UUID.randomUUID().toString().replaceAll("-", "") + ".png";
        else
            throw new RuntimeException();
        ImageUtil.addContentToImage(bgImagePath, results, experienceTime, "苹方 常规", 26, 14, 868, 255, 255, 255);
        return qiNiuService.upload(results, null, resourceBucketName);
    }

    @Override
    public Integer getUserVirtualPoint(Long userId) {
        Integer virtualPoint = 0;
        List<UserRechargeRecord> userRechargeRecordList = userRechargeRecordService.getUserRechargeRecordListByUserId(userId);
        List<UserConsumeRecord> userConsumeRecordList = userConsumeRecordService.getUserConsumeRecordListByUserId(userId);
        for (UserRechargeRecord userRechargeRecord : userRechargeRecordList) {
            virtualPoint += userRechargeRecord.getRechargePoint();
        }
        for (UserConsumeRecord userConsumeRecord : userConsumeRecordList) {
            virtualPoint -= userConsumeRecord.getConsumePoint();
        }
        return virtualPoint < 0 ? 0 : virtualPoint;
    }

    @Override
    public void saveUserBranchSaleChange(Long userId, Integer oldBranchsalerId, Integer newBranchsalerId) {
        try {
            if (oldBranchsalerId != null && newBranchsalerId != null && !newBranchsalerId.equals(oldBranchsalerId)) {
                UserBranchSaleChange userBranchSaleChange = new UserBranchSaleChange();
                userBranchSaleChange.setUserId(userId).setOldBranchsalerId(oldBranchsalerId).setNewBranchsalerId(newBranchsalerId);
                userBranchSaleChange.setType(UserBranchSalerChangeTypeStatus.PAY_CHANGE.getIndex());
                userBranchSaleChangeService.saveUserBranchSaleChange(userBranchSaleChange);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
    }

    @Override
    public User createAnonymityUser() {
        User user = new User();
        user.setName("游客" + UniqueKeyGeneratorUtil.random(3)).setHeadImage(defaultAvatar).setCountry("中国").setRole(0).setStatus(0);
        user.setRegisterEntry(RegisterEntryStatus.IOS.getIndex()).setBranchsaler(1);
        return user;
    }

    @Override
    public Integer getPayEntry(@NotNull String origin) {
        for (PayEntryStatus value : PayEntryStatus.values()) {
            if (value.getMsg().equalsIgnoreCase(origin)) {
                return value.getIndex();
            }
        }
        return null;
    }

    @Override
    public UserOrder createUserOrder(Long userId, Integer bookId, BigDecimal price, String origin, Long ambassadorId, UserOrderPayRoleStatus payRole) {
        //确保不是扫了销售大使的支付码
        if (ambassadorId == null || !judgeUserIsSaleAmbassador(ambassadorId)) {
            User user = userService.getUserById(userId);
            if (user != null) {
                Long introducerId = user.getRegisterIntroducer();
                //确保邀请支付的人目前是销售大使
                if (introducerId != null && judgeUserIsSaleAmbassador(introducerId)) {
                    User introducer = userService.getUserById(introducerId);
                    //确保在同一个分会
                    if (introducer != null && introducer.getBranchsaler() != null && user.getBranchsaler() != null && user.getBranchsaler().equals(introducer.getBranchsaler())) {
                        ambassadorId = introducerId;
                        payRole = UserOrderPayRoleStatus.SALE_ROLE;
                    }
                }
            }
        }
        UserOrder userOrder = new UserOrder();
        String now = LocalDateTimeUtil.getCurrentTimestamp();
        userOrder.setOrderno(MD5Util.getMD5Code(UniqueKeyGeneratorUtil.random(10) + System.currentTimeMillis()));
        userOrder.setUserid(userId).setBookid(bookId).setFee(price).setState(UserOrderStatus.PREPARE_PAY.getIndex()).setPaysource(getPayEntry(origin));
        userOrder.setCreatetime(now).setUpdatetime(now).setPayrole(payRole.getIndex()).setIntroducerid(ambassadorId == null ? 0 : ambassadorId);
        User user = userService.getUserById(ambassadorId == null ? userId : ambassadorId);
        if (user != null) {
            userOrder.setBranchsalerId(user.getBranchsaler());
        }
        return userOrder;
    }

    @Override
    public Long getUserIdNoNeedLogin(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token))
            return null;
        return userLoginService.getUserLoginByToken(token);
    }

    @Override
    public Long getUserIdNeedLogin(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    @Override
    public User getUserNeedLogin(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserById(userId);
    }

    @Override
    public void paySuccessAfter(String orderNo) {
        UserOrder userOrder = userOrderService.getUserOrderByOrderNo(orderNo);
        if (userOrder != null && userOrder.getState() != null && userOrder.getState().equals(UserOrderStatus.PREPARE_PAY.getIndex())) {
            userOrder.setState(UserOrderStatus.COMPLETE_PAY.getIndex()).setUpdatetime(LocalDateTimeUtil.getCurrentTimestamp());
            userOrderService.updateUserOrder(userOrder);
            Long userId = userOrder.getUserid();
            Integer bookId = userOrder.getBookid();
            User user = userService.getUserById(userId);
            if (userOrder.getPayrole().equals(UserOrderPayRoleStatus.KNOWLEDGE_GIFT_ROLE.getIndex())) {
                String args = "phone";
                if (orderNo.contains(args)) {
                    String phone = orderNo.substring(orderNo.indexOf(args) + args.length());
                    User temp = userService.getUserByPhone(phone);
                    if (temp == null) {
                        Map<String, String> map = PhoneUtil.getProvinceAndCityByPhone(phone);
                        if (map != null && StringUtils.isNotEmpty(map.get("province")) && StringUtils.isNotEmpty(map.get("city"))) {
                            temp = createUser(phone, map.get("province"), map.get("city"), null, "wap", UniqueKeyGeneratorUtil.generateToken());
                            temp.setBranchsaler(user.getBranchsaler());
                            userService.updateUser(temp);
                        }
                    }
                    if (temp != null)
                        openCourseVipPermission(temp.getId(), bookId);
                }
            } else
                openCourseVipPermission(userId, bookId);
            if (userOrder.getPayrole().equals(UserOrderPayRoleStatus.SPREAD_ROLE.getIndex()) && userOrder.getIntroducerid() != null && !(userOrder.getIntroducerid().equals(userId))) {
                Long ambassadorId = userOrder.getIntroducerid();
                IntegralOrigin integration = new IntegralOrigin();
                integration.setUserId(ambassadorId).setInUserId(userId).setIntegral(IntegrationTypeStatus.PURCHASE.getIntegration());
                integration.setType(IntegrationTypeStatus.PURCHASE.getIndex());
                integralService.saveIntegral(integration);
            }
            Long ambassadorId = userOrder.getIntroducerid();
            if (ambassadorId != null && ambassadorId > 0) {
                User ambassador = userService.getUserById(ambassadorId);
                if (ambassador != null && userOrder.getFee().compareTo(new BigDecimal("98")) > 0) {
                    saveUserBranchSaleChange(user.getId(), user.getBranchsaler(), ambassador.getBranchsaler());
                    user.setBranchsaler(ambassador.getBranchsaler());
                    userService.updateUser(user);
                }
                //在这里写发送短信通知吧try-catch
                try {
                    sendPayNotice(userOrder, user, bookService.getBookById(bookId));
                } catch (Exception e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
            DistributionMessageBean messageBean = new DistributionMessageBean();
            messageBean.setUserId(userId.intValue()).setBranchsalerId(user.getBranchsaler()).setAmount(userOrder.getFee()).setType("0");
            messageBean.setSalerType("1").setCreatetime(new Date()).setOrderno(userOrder.getOrderno());
            if (userOrder.getPayrole().equals(UserOrderPayRoleStatus.SPREAD_ROLE.getIndex()))
                messageBean.setSalerType("0");
            if (userOrder.getPaysource() != null && userOrder.getPaysource().equals(PayEntryStatus.IOS.getIndex()))
                messageBean.setAmount(userOrder.getFee().multiply(new BigDecimal("0.7")));
            payMessageService.sendPaySuccessMessage(messageBean);
        }
    }

    @Override
    public Boolean judgeUserPermissionWatchVipBookChapterPart(Integer bookId, HttpServletRequest request) {
        Long userId = getUserIdNoNeedLogin(request);
        if (userId == null)
            return Boolean.FALSE;
        List<UserBook> list = userBookService.getUserBookListByUserId(userId);
        UserBook userBook = new UserBook();
        Optional<UserBook> optional = list.stream().filter(ub -> ub.getBookId().equals(bookId)).findFirst();
        if (optional.isEmpty()) {
            optional = list.stream().filter(ub -> ub.getType().equals(UserBookStatus.EXP.getIndex())).findFirst();
        }
        if (optional.isEmpty()) {
            optional = list.stream().findFirst();
            optional.ifPresent(ub -> {
                LocalDateTime startTime = LocalDateTimeUtil.parseLocalDateTime(ub.getStartTime());
                if (startTime != null) {
                    userBook.setEndTime(LocalDateTimeUtil.formatLocalDateTime(startTime.plusDays(7)));
                }
            });
        } else {
            optional.ifPresent(ub -> userBook.setEndTime(ub.getEndTime()));
        }
        LocalDateTime endTime = LocalDateTimeUtil.parseLocalDateTime(userBook.getEndTime());
        if (endTime != null && endTime.isAfter(LocalDateTime.now()))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Override
    public Boolean judgeUserPurchaseBookNoNeedLogin(Integer bookId, HttpServletRequest request) {
        Long userId = getUserIdNoNeedLogin(request);
        if (userId != null) {
            UserBook userBook = userBookService.getUserBookListByUserIdAndBookId(userId, bookId);
            if (userBook != null) {
                return userBook.getType().equals(UserBookTypeStatus.VIP.getIndex());
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Integer userToBookStatus(@NotNull Integer bookId, HttpServletRequest request) {
        Long userId = getUserIdNoNeedLogin(request);
        if (userId == null)
            return 0;
        UserBook userBook = userBookService.getUserBookListByUserIdAndBookId(userId, bookId);
        if (userBook != null) {
            if (UserBookStatus.VIP.getIndex().equals(userBook.getType()))
                return 3;
            else {
                LocalDateTime localDateTime = LocalDateTimeUtil.parseLocalDateTime(userBook.getEndTime());
                if (localDateTime != null && localDateTime.isAfter(LocalDateTime.now()))
                    return 1;
                else
                    return 2;
            }
        }
        List<UserBook> list = userBookService.getUserBookListByUserId(userId);
        UserBook uBook = new UserBook();
        Optional<UserBook> optional = list.stream().filter(ub -> ub.getType().equals(UserBookStatus.EXP.getIndex())).findFirst();
        if (optional.isEmpty())
            list.stream().findFirst().ifPresent(ub -> uBook.setEndTime(ub.getStartTime()));
        else
            optional.ifPresent(ub -> uBook.setEndTime(ub.getEndTime()));
        LocalDateTime localDateTime = LocalDateTimeUtil.parseLocalDateTime(uBook.getEndTime());
        if (localDateTime != null && localDateTime.isAfter(LocalDateTime.now()))
            return 1;
        return 2;
    }

    @Override
    public KnowledgeContentVo getKnowledgeContentByBookIdAndChapterIdAndPartId(Integer bookId, Integer chapterId, Integer partId) {
        KnowledgeContentVo result = new KnowledgeContentVo();
        List<BookChapterPartArchitecture> architectureList = bookChapterPartArchitectureService.getBookChapterPartArchitectureListByBookIdAndChapterIdAndPartId(bookId, chapterId, partId);
        if (architectureList == null || architectureList.size() == 0)
            return result;
        Map<Integer, List<BookChapterPartArchitecture>> map = architectureList.stream().collect(Collectors.groupingBy(BookChapterPartArchitecture::getType));
        for (Map.Entry<Integer, List<BookChapterPartArchitecture>> entry : map.entrySet()) {
            Integer type = entry.getKey();
            architectureList = entry.getValue();
            if (type.equals(BookChapterPartArchitectureTypeStatus.KNOWLEDGE_EXTRACT.getIndex())) {
                result.setImageUrl(architectureList.get(0).getImgUrl());
            } else if (type.equals(BookChapterPartArchitectureTypeStatus.COURSE_TEST.getIndex())) {
                List<CourseTestVo> courseTestList = new ArrayList<>();
                for (BookChapterPartArchitecture bcp : architectureList) {
                    CourseTestVo ct = new CourseTestVo();
                    ct.setTitle(bcp.getTitle()).setOption(bcp.getOption()).setAnswer(bcp.getAnswer());
                    courseTestList.add(ct);
                }
                result.setCourseTestList(courseTestList);
            }
        }
        return result;
    }

    @Override
    public String getBookChapterPartPlayUrl(BookChapterPart target, HttpServletRequest request) {
        String playUrl = null;
        if (target.getState().equals(BookChapterPartStatus.FREE.getIndex())) {
            playUrl = qiNiuService.getBucket(vipBucketName).getDownloadUrl(target.getVipUrl());
        } else {
            boolean flag = judgeUserPermissionWatchVipBookChapterPart(target.getBookId(), request);
            if (flag)
                playUrl = qiNiuService.getBucket(vipBucketName).getDownloadUrl(target.getVipUrl());
        }
        return playUrl;
    }

    @Override
    public void saveOrUpdateUserPlayRecord(Integer bookId, Integer bookChapterId, Integer bookChapterPartId, HttpServletRequest request) {
        Long userId = getUserIdNoNeedLogin(request);
        if (userId == null)
            return;
        UserPlayRecord userPlayRecord = userPlayRecordService.getUserPlayRecordByUserIdAndBookIdAndChapterIdAndPartId(userId, bookId, bookChapterId, bookChapterPartId);
        if (userPlayRecord == null) {
            userPlayRecord = new UserPlayRecord();
            userPlayRecord.setUserId(userId).setBookId(bookId).setChapterId(bookChapterId);
            userPlayRecord.setPartId(bookChapterPartId).setPlayCount(1);
            userPlayRecordService.saveUserPlayRecord(userPlayRecord);
        } else {
            userPlayRecord.setPlayCount(userPlayRecord.getPlayCount() + 1);
            userPlayRecordService.updateUserPlayRecord(userPlayRecord);
        }
    }

    @Override
    public Boolean mkdirs(String filePath) {
        File bgImage = new File(filePath);
        if (!bgImage.exists()) {
            return bgImage.mkdirs();
        }
        return Boolean.TRUE;
    }

    public WeChatPayUnifiedOrderParams createWeChatPayUnifiedOrderParams(UserOrder userOrder, String appId, String mchId, String tradeType, String openId, String callBackUrl, Integer bookId, HttpServletRequest request) {
        WeChatPayUnifiedOrderParams weChatPayUnifiedOrderParams = new WeChatPayUnifiedOrderParams();
        weChatPayUnifiedOrderParams.setAppId(appId);
        weChatPayUnifiedOrderParams.setMchId(mchId);
        weChatPayUnifiedOrderParams.setNotifyUrl(callBackUrl);
        weChatPayUnifiedOrderParams.setBody("购买《" + bookService.getBookById(bookId).getName() + "》课程");
        weChatPayUnifiedOrderParams.setOutTradeNo(userOrder.getOrderno());
        weChatPayUnifiedOrderParams.setNonceStr(UniqueKeyGeneratorUtil.generateToken());
        weChatPayUnifiedOrderParams.setTotalFee(userOrder.getFee().multiply(new BigDecimal("100")).intValue());
        weChatPayUnifiedOrderParams.setSpbillCreateIp(HttpClientUtils.getRealIpAddr(request));
        weChatPayUnifiedOrderParams.setTradeType(tradeType).setOpenId(openId);
        return weChatPayUnifiedOrderParams;
    }

    @Override
    public void weChatCallBackHandle(String key, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String SUCCESS = "SUCCESS";
        String requestStr = WeChatPayUtils.getXmlFromRequest(request);
        Map<String, String> parameterMap = XmlConvertUtil.convertToMap(requestStr);
        String returnCode = parameterMap.get("return_code");
        if (!returnCode.equals(SUCCESS))
            return;
        String sign = parameterMap.get("sign");
        boolean flag = WeChatPayUtils.verifyMapParam(parameterMap, key, sign);
        if (!flag)
            return;
        WeChatPayResult result = WeChatPayUtils.xmlToWeChatPayResult(requestStr);
        if (result != null && result.getResultCode().equals(SUCCESS)) {
            String orderNo = result.getOutTradeNo();
            paySuccessAfter(orderNo);
            Map<String, String> returnResult = new HashMap<>();
            returnResult.put("return_code", SUCCESS);
            returnResult.put("return_msg", "OK");
            String return_result = WeChatPayUtils.map2Xml(returnResult);
            response.setContentType(ContentType.TEXT_XML.getMimeType());
            response.setContentLength(return_result.length());
            response.getWriter().print(return_result);
            response.getWriter().close();
        }
    }

    @Override
    public Integer getUserRegisterBelongsBranch(String province, String city) {
        ProvinceCityBranchMapping mappingInfo = provinceCityBranchMappingService.getProvinceCityBranchMappingByProvinceAndCity(province, city);
        if (mappingInfo != null && mappingInfo.getBranchSaleId() != null) {
            return mappingInfo.getBranchSaleId();
        }
        Integer branch = 1;
        List<BranchSaler> citySales = branchSalerService.getCityBranchSalerByName(city);
        if (citySales != null && citySales.size() > 0) {
            if (judgeCityIsMunicipality(city))
                branch = citySales.get(0).getId();
            else {
                //得到省分会
                List<BranchSaler> provinceSales = branchSalerService.getBranchSalerByIds(citySales.stream().map(BranchSaler::getPid).collect(Collectors.toSet()));
                //loop可能没有匹配上
                for (BranchSaler bs : provinceSales) {
                    if (!org.springframework.util.StringUtils.isEmpty(bs.getSimplename()) && bs.getSimplename().equalsIgnoreCase(province)) {
                        for (BranchSaler br : citySales) {
                            if (br.getPid() != null && bs.getId() != null && br.getPid().equals(bs.getId())) {
                                branch = br.getId();
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (branch == 1) {
            BranchSaler provinceSale = branchSalerService.getProvinceBranchSalerByName(province);
            if (provinceSale != null)
                branch = provinceSale.getId();
        }
        return branch;
    }

    @Override
    public User createUser(String phone, String province, String city, Long ambassadorId, String system, String token) {
        if (StringUtils.isNotEmpty(city) && city.equals("巴音郭楞")) {
            city = "巴州";
        }
        User user = new User();
        user.setName("半间教室" + UniqueKeyGeneratorUtil.random(3));
        user.setPhone(phone).setHeadImage(defaultAvatar);
        user.setCountry("中国").setProvince(province).setCity(city).setBranchsaler(1);
        if (system.equalsIgnoreCase("ios"))
            user.setRegisterEntry(RegisterEntryStatus.IOS.getIndex());
        else if (system.equalsIgnoreCase("android"))
            user.setRegisterEntry(RegisterEntryStatus.ANDROID.getIndex());
        else if (system.equalsIgnoreCase("wap"))
            user.setRegisterEntry(RegisterEntryStatus.WAP.getIndex());
        else if (system.equalsIgnoreCase("card"))
            user.setRegisterEntry(RegisterEntryStatus.CARD.getIndex());
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(LocalDateTimeUtil.formatLocalDateTime(now)).setUpdateTime(LocalDateTimeUtil.formatLocalDateTime(now)).setBranchsaler(getUserRegisterBelongsBranch(province, city)).setStatus(0).setRole(0);
        userService.saveUser(user);
        user = userService.getUserById(user.getId());
        userLoginService.saveOrUpdateUserLogin(user.getId(), token);
        List<Book> bookList = bookService.getAllBookList();
        Set<Integer> bookIds = bookList.stream().map(Book::getId).collect(Collectors.toSet());
        List<UserBook> userBooks = new ArrayList<>();
        String endTime = LocalDateTimeUtil.formatLocalDateTime(now.plusDays(7));
        for (Integer bookId : bookIds) {
            UserBook userBook = new UserBook();
            userBook.setBookId(bookId).setUserId(user.getId()).setType(UserBookStatus.EXP.getIndex()).setStartTime(LocalDateTimeUtil.formatLocalDateTime(now)).setEndTime(endTime);
            userBooks.add(userBook);
        }
        userBookService.batchSaveUserBook(userBooks);
        if (ambassadorId != null) {
            User ambassador = userService.getUserById(ambassadorId);
            if (ambassador != null) {
                user.setRegisterIntroducer(ambassadorId).setBranchsaler(ambassador.getBranchsaler());
                userService.updateUser(user);
                Integer role = ambassador.getRole();
                if (role != null && role.equals(UserRoleStatus.SPREAD_AMBASSADOR.getIndex())) {
                    IntegralOrigin integration = new IntegralOrigin();
                    integration.setUserId(ambassadorId).setInUserId(user.getId());
                    integration.setType(IntegrationTypeStatus.SHARE.getIndex()).setIntegral(IntegrationTypeStatus.SHARE.getIntegration());
                    integralService.saveIntegral(integration);
                }
                //在这里写发送短信的通知，try-catch
                try {
                    if (role != null) {
                        String timestamp = LocalDateTimeUtil.getCurrentTimestamp();
                        String date = LocalDateTimeUtil.formatLocalDate(LocalDateTimeUtil.parseLocalDate(timestamp));
                        Long count = userService.getAmbassadorSpreadUserCount(ambassadorId);
                        Long integral = 0L;
                        List<IntegralOrigin> integralOriginList = integralService.getIntegralListByUserId(ambassadorId);
                        for (IntegralOrigin integralOrigin : integralOriginList) {
                            integral += integralOrigin.getIntegral();
                        }
                        sendAmbassadorNotice(role.equals(UserRoleStatus.SALE_AMBASSADOR.getIndex()), false, ambassador.getPhone(), user.getName(), user.getPhone(), timestamp, null, date, count, integral);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }
        return user;
    }

    @Override
    public void openCourseVipPermission(Long userId, Integer bookId) {
        UserBook userBook = userBookService.getUserBookListByUserIdAndBookId(userId, bookId);
        if (userBook == null) {
            userBook = new UserBook();
            String endTime = LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now().plusYears(100));
            userBook.setUserId(userId).setBookId(bookId).setType(UserBookStatus.VIP.getIndex()).setStartTime(LocalDateTimeUtil.getCurrentTimestamp()).setEndTime(endTime);
            userBookService.saveUserBook(userBook);
        } else {
            userBook.setType(UserBookStatus.VIP.getIndex());
            String endTime = LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now().plusYears(100));
            userBook.setEndTime(endTime);
            userBookService.updateUserBook(userBook);
        }
    }

    @Override
    public boolean judgeUserWhetherPurchaseBookByUserIdAndBookId(@NotNull Long userId, @NotNull Integer bookId) {
        UserBook userBook = userBookService.getUserBookListByUserIdAndBookId(userId, bookId);
        if (userBook != null && userBook.getType() != null && userBook.getType().equals(UserBookStatus.VIP.getIndex()))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Override
    public String getShareImageUrl(ShareInfo shareInfo, String key, String url, String... contents) throws RuntimeException {
        final String PREFIX_PATH = File.separator + "opt" + File.separator + "classroom" + File.separator + "image";
        String result;
        String imageUrl = shareInfo.getBgImageName();
        FileInfo fileInfo = qiNiuService.getFileInfoByKey(key, resourceBucketName);
        if (fileInfo != null) {
            return qiNiuService.getBucket(resourceBucketName).getDownloadUrl(key);
        }
        String bgImagePath = PREFIX_PATH + File.separator + "bg" + File.separator;
        if (mkdirs(bgImagePath))
            bgImagePath += MD5Util.getMD5Code(imageUrl);
        else
            throw new RuntimeException();
        if (!new File(bgImagePath).exists())
            qiNiuService.downloadFile(imageUrl, bgImagePath);
        String codePath = PREFIX_PATH + File.separator + "code" + File.separator;
        if (mkdirs(codePath))
            codePath += key;
        else
            throw new RuntimeException();
        if (!new File(codePath).exists())
            QRCodeUtil.encode(url, codePath);
        String resultQrCode = PREFIX_PATH + File.separator + "result_qr_code" + File.separator;
        if (mkdirs(resultQrCode))
            resultQrCode += key;
        else
            throw new RuntimeException();
        ImageUtil.composeImage(bgImagePath, codePath, resultQrCode, shareInfo.getQrX(), shareInfo.getQrY(), shareInfo.getQrW(), shareInfo.getQrH());
        result = resultQrCode;
        if (contents != null && contents.length > 0) {
            String resultContent = PREFIX_PATH + File.separator + "result_content" + File.separator;
            if (mkdirs(resultContent))
                resultContent += key;
            else
                throw new RuntimeException();
            ImageUtil.addContentToImage(resultQrCode, resultContent, contents[0], shareInfo.getFontStyle(), shareInfo.getFontSize(), shareInfo.getFontX(), shareInfo.getFontY(), shareInfo.getFontColorR(), shareInfo.getFontColorG(), shareInfo.getFontColorB());
            result = resultContent;
            if (contents.length > 1) {
                String resultDays = PREFIX_PATH + File.separator + "result_days" + File.separator;
                if (mkdirs(resultDays))
                    resultDays += key;
                else
                    throw new RuntimeException();
                ImageUtil.addContentToImage(resultContent, resultDays, contents[1], shareInfo.getFontStyleNext(), shareInfo.getFontSizeNext(), shareInfo.getFontXNext() + (74 - 14 * contents[1].length()) / 2, shareInfo.getFontYNext(), shareInfo.getFontColorRNext(), shareInfo.getFontColorGNext(), shareInfo.getFontColorBNext());
                result = resultDays;
            }
            url = qiNiuService.upload(result, key, resourceBucketName);
        } else
            url = qiNiuService.upload(result, key, resourceBucketName);
        return url;
    }

    @Override
    public Integer getBookIdByAppleKey(String key) {
        if (StringUtils.isNotBlank(key)) {
            if (key.equals("com.TingYun.ClassroomHalf_198"))
                return 1;
            if (key.equals("com.TingYun.ClassroomHalf_298"))
                return 24;
            if (key.equals("com.TingYun.ClassroomHalf_198_1"))
                return 25;
        }
        return null;
    }

    @Override
    public boolean judgeUserIsSaleAmbassador(Long userId) {
        if (userId != null) {
            User user = userService.getUserById(userId);
            return (user != null && user.getRole() != null && user.getRole().equals(UserRoleStatus.SALE_AMBASSADOR.getIndex()));
        }
        return false;
    }

    private Boolean judgeCityIsMunicipality(String city) {
        Boolean flag = Boolean.FALSE;
        String[] cities = {"北京", "上海", "重庆", "天津"};
        if (!StringUtils.isEmpty(city)) {
            flag = Arrays.asList(cities).contains(city);
        }
        return flag;
    }

    private void sendNoticeRequest(String tplId, String tplValue, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("tpl_id", tplId);
        params.put("tpl_value", tplValue);
        params.put("mobile", phone);
        HttpClientUtils.post(sendCodeUrl, params);
    }

    private void sendAmbassadorNotice(boolean salesAmbassador, boolean pay, String ambassadorPhone,
                                      String userName, String phone, String timestamp, String bookName,
                                      String date, Long count, Long integral) throws UnsupportedEncodingException {
        String value = StringUtils.EMPTY;
        Integer tplId;
        String templateCode;
        if (pay) {
            tplId = PhoneMessageStatus.SALES_AMBASSADOR_INVITE_PAY.getIndex();
            templateCode = AliMessageStatus.SALES_AMBASSADOR_INVITE_PAY.getIndex();
            if (StringUtils.isNotEmpty(userName) && StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(bookName) && StringUtils.isNotBlank(date) && count != null) {
                value = URLEncoder.encode("#timestamp#", encoding) + "=" + URLEncoder.encode(timestamp, encoding) + "&" +
                        URLEncoder.encode("#bookName#", encoding) + "=" + URLEncoder.encode(bookName, encoding) + "&" +
                        URLEncoder.encode("#date#", encoding) + "=" + URLEncoder.encode(date, encoding) + "&" +
                        URLEncoder.encode("#count#", encoding) + "=" + URLEncoder.encode(count.toString(), encoding) + "&" +
                        URLEncoder.encode("#userName#", encoding) + "=" + URLEncoder.encode(userName, encoding) + "&" +
                        URLEncoder.encode("#phone#", encoding) + "=" + URLEncoder.encode(phone, encoding);
            }
            if (StringUtils.isNotBlank(value) && !salesAmbassador) {
                templateCode = AliMessageStatus.PROMOTION_AMBASSADOR_INVITE_PAY.getIndex();
                tplId = PhoneMessageStatus.PROMOTION_AMBASSADOR_INVITE_PAY.getIndex();
                if (integral != null)
                    value = value + "&" + URLEncoder.encode("#integral#", encoding) + "=" + URLEncoder.encode(integral.toString(), encoding);
                else
                    value = StringUtils.EMPTY;
            }
        } else {
            if (salesAmbassador) {
                templateCode = AliMessageStatus.SALES_AMBASSADOR_INVITE_REGISTER.getIndex();
                tplId = PhoneMessageStatus.SALES_AMBASSADOR_INVITE_REGISTER.getIndex();
                if (StringUtils.isNotEmpty(userName) && StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(date) && count != null) {
                    value = URLEncoder.encode("#userName#", encoding) + "=" + URLEncoder.encode(userName, encoding) + "&" +
                            URLEncoder.encode("#phone#", encoding) + "=" + URLEncoder.encode(phone, encoding) + "&" +
                            URLEncoder.encode("#date#", encoding) + "=" + URLEncoder.encode(date, encoding) + "&" +
                            URLEncoder.encode("#count#", encoding) + "=" + URLEncoder.encode(count.toString(), encoding);
                }
            } else {
                tplId = PhoneMessageStatus.PROMOTION_AMBASSADOR_INVITE_REGISTER.getIndex();
                templateCode = AliMessageStatus.PROMOTION_AMBASSADOR_INVITE_REGISTER.getIndex();
                if (StringUtils.isNotBlank(date) && count != null && integral != null) {
                    value = URLEncoder.encode("#date#", encoding) + "=" + URLEncoder.encode(date, encoding) + "&" +
                            URLEncoder.encode("#count#", encoding) + "=" + URLEncoder.encode(count.toString(), encoding) + "&" +
                            URLEncoder.encode("#integral#", encoding) + "=" + URLEncoder.encode(integral.toString(), encoding);
                }
            }
        }
        Map<String, String> temp = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        temp.put("userName", userName);
        temp.put("phone", phone);
        temp.put("timestamp", timestamp);
        temp.put("bookName", bookName);
        temp.put("date", date);
        temp.put("count", count != null ? count.toString() : "");
        temp.put("integral", integral != null ? integral.toString() : "");
        for (Map.Entry<String, String> entry : temp.entrySet()) {
            if (StringUtils.isNotBlank(entry.getValue())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        if (StringUtils.isNotBlank(value) && tplId != null && StringUtils.isNotBlank(ambassadorPhone)) {
            boolean flag = sendAliMessage(ambassadorPhone, templateCode, JSONUtil.toJSONString(result));
            if (!flag) {
                sendNoticeRequest(tplId.toString(), value, ambassadorPhone);
            }
        }
    }

    private boolean sendAliMessage(String phone, String templateCode, String templateParam) {
        boolean result = false;
        try {
            DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setProtocol(ProtocolType.HTTPS);
            request.setMethod(MethodType.POST);
            request.setDomain(domain);
            request.setVersion(version);
            request.setAction(action);
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", templateParam);
            for (int i = 1; i <= 10; i++) {
                if (i < 10) {
                    request.putQueryParameter("SignName", "半间教室0" + i);
                } else {
                    request.putQueryParameter("SignName", "半间教室" + i);
                }
                CommonResponse response = client.getCommonResponse(request);
                boolean flag = JSONUtil.parseObject(response.getData()).getString("Code").equals("OK");
                if (flag) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return result;
    }
}
