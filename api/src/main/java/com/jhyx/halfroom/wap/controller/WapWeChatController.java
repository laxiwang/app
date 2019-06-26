package com.jhyx.halfroom.wap.controller;

import com.jhyx.halfroom.bean.User;
import com.jhyx.halfroom.bean.UserOrder;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.constant.UserOrderPayRoleStatus;
import com.jhyx.halfroom.constant.UserRoleStatus;
import com.jhyx.halfroom.service.CommonService;
import com.jhyx.halfroom.service.UserOrderService;
import com.jhyx.halfroom.service.UserService;
import com.jhyx.halfroom.service.WeChatService;
import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping(value = "/v4/wap/wx")
@RequiredArgsConstructor
@Slf4j
public class WapWeChatController {
    private final WeChatService weChatService;
    private final CommonService commonService;
    private final UserService userService;
    private final UserOrderService userOrderService;
    @Value("${wechat.wap.appId}")
    private String appId;
    @Value("${wechat.wap.mchId}")
    private String mchId;
    @Value("${wechat.wap.key}")
    private String key;
    @Value("${wechat.wap.callBackUrl}")
    private String callBackUrl;

    @GetMapping(value = "/share/sign", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Map<String, String>> wapShareSign(@RequestParam String url) {
        Map<String, String> result;
        try {
            result = weChatService.wapShareSign(url);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @PostMapping(value = "/pay/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Map<String, String>> createOrder(@RequestParam Integer bookId,
                                                   @RequestParam BigDecimal price,
                                                   @RequestParam String origin,
                                                   @RequestParam String code,
                                                   @RequestParam(required = false) String phone,
                                                   @RequestParam(required = false) Long ambassadorId,
                                                   HttpServletRequest request) {
        Map<String, String> result;
        try {
            UserOrder userOrder;
            Long userId = (Long) request.getAttribute("userId");
            boolean purchase = commonService.judgeUserPurchaseBookByUserIdOrPhone(phone, userId, bookId);
            if (purchase) {
                return new Result<>(ResultCode.USER_HAS_PURCHASE_BOOK.getIndex(), ResultCode.USER_HAS_PURCHASE_BOOK.getMsg(), null);
            }
            if (ambassadorId != null) {
                User user = userService.getUserById(ambassadorId);
                boolean flag = user.getRole().equals(UserRoleStatus.SPREAD_AMBASSADOR.getIndex());
                userOrder = commonService.createUserOrder(userId, bookId, price, origin, ambassadorId, flag ? UserOrderPayRoleStatus.SPREAD_ROLE : UserOrderPayRoleStatus.SALE_ROLE);
            } else {
                userOrder = commonService.createUserOrder(userId, bookId, price, origin, null, UserOrderPayRoleStatus.NATURE_ROLE);
            }
            userOrderService.saveUserOrder(userOrder);
            if (StringUtils.isNotEmpty(phone)) {
                commonService.updateKnowledgeGiftUserOrder(userOrder, phone);
            }
            String openId = weChatService.getWapOpenId(code);
            WeChatPayUnifiedOrderParams weChatPayUnifiedOrderParams = commonService.createWeChatPayUnifiedOrderParams(userOrder, appId, mchId, "JSAPI", openId, callBackUrl, bookId, request);
            result = weChatService.signWapPay(weChatPayUnifiedOrderParams);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @PostMapping(value = "/pay/weChatWapCallBack", produces = MediaType.APPLICATION_XML_VALUE)
    public void weChatWapCallBack(HttpServletRequest request, HttpServletResponse response) {
        try {
            commonService.weChatCallBackHandle(key, request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
    }
}
