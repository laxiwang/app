package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.constant.UserOrderPayRoleStatus;
import com.jhyx.halfroom.vo.app.StudyPunchTimeVo;
import com.jhyx.halfroom.vo.common.KnowledgeContentVo;
import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public interface CommonService {
    Integer getUserAnswerInfoStatus(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId);

    void sendPayNotice(UserOrder userOrder, User user, Book book) throws UnsupportedEncodingException;

    void sendMessageCode(String phone, String code) throws UnsupportedEncodingException;

    void updateKnowledgeGiftUserOrder(UserOrder userOrder, String phone);

    boolean judgeUserPurchaseBookByUserIdOrPhone(String phone, Long userId, Integer bookId);

    StudyPunchTimeVo getUserStudyPunchTime(@NotNull Long userId);

    Integer getPayEntry(@NotNull String origin);

    UserOrder createUserOrder(Long userId, Integer bookId, BigDecimal price, String origin, Long ambassadorId, UserOrderPayRoleStatus payRole);

    Long getUserIdNoNeedLogin(HttpServletRequest request);

    Long getUserIdNeedLogin(HttpServletRequest request);

    User getUserNeedLogin(HttpServletRequest request);

    void paySuccessAfter(String orderNo);

    Boolean judgeUserPermissionWatchVipBookChapterPart(Integer bookId, HttpServletRequest request);

    Boolean judgeUserPurchaseBookNoNeedLogin(Integer bookId, HttpServletRequest request);

    Integer userToBookStatus(@NotNull Integer bookId, HttpServletRequest request);

    KnowledgeContentVo getKnowledgeContentByBookIdAndChapterIdAndPartId(Integer bookId, Integer chapterId, Integer partId);

    String getBookChapterPartPlayUrl(BookChapterPart target, HttpServletRequest request);

    void saveOrUpdateUserPlayRecord(Integer bookId, Integer bookChapterId, Integer bookChapterPartId, HttpServletRequest request);

    Boolean mkdirs(String filePath);

    WeChatPayUnifiedOrderParams createWeChatPayUnifiedOrderParams(UserOrder userOrder, String appId, String mchId, String tradeType, String openId, String callBackUrl, Integer bookId, HttpServletRequest request);

    void weChatCallBackHandle(String key, HttpServletRequest request, HttpServletResponse response) throws Exception;

    Integer getUserRegisterBelongsBranch(String province, String city);

    User createUser(String phone, String province, String city, Long ambassadorId, String system, String token);

    void openCourseVipPermission(Long userId, Integer bookId);

    boolean judgeUserWhetherPurchaseBookByUserIdAndBookId(@NotNull Long userId, @NotNull Integer bookId);

    String getShareImageUrl(ShareInfo shareInfo, String key, String url, String... contents) throws RuntimeException;

    User createAnonymityUser();

    Integer getBookIdByAppleKey(String key);

    void saveUserBranchSaleChange(Long userId, Integer oldBranchsalerId, Integer newBranchsalerId);

    Integer getUserVirtualPoint(Long userId);

    String getUserRegisterTipsImageUrl(String experienceTime);

    String getUserStudyPunchTimeCardImageUrl(@NotNull Long userId);

    boolean judgeUserIsSaleAmbassador(Long userId);
}
