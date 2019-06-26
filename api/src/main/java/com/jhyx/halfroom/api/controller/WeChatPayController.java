package com.jhyx.halfroom.api.controller;

import com.jhyx.halfroom.bean.UserOrder;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.constant.UserOrderPayRoleStatus;
import com.jhyx.halfroom.constant.UserOrderStatus;
import com.jhyx.halfroom.service.BookService;
import com.jhyx.halfroom.service.CommonService;
import com.jhyx.halfroom.service.UserOrderService;
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
@RequestMapping(value = "/v4/api/wx/pay")
@RequiredArgsConstructor
@Slf4j
public class WeChatPayController {
    private final UserOrderService userOrderService;
    private final WeChatService weChatService;
    private final CommonService commonService;
    private final BookService bookService;
    @Value("${wechat.app.appId}")
    private String appId;
    @Value("${wechat.app.mchId}")
    private String mchId;
    @Value("${wechat.app.key}")
    private String key;
    @Value("${wechat.app.callBackUrl}")
    private String callBackUrl;

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Map<String, String>> createOrder(@RequestParam Integer bookId,
                                                   @RequestParam String origin,
                                                   @RequestParam(required = false, defaultValue = "") String phone,
                                                   HttpServletRequest request) {
        Map<String, String> result;
        try {
            Long userId = (Long) request.getAttribute("userId");
            boolean purchase = commonService.judgeUserPurchaseBookByUserIdOrPhone(phone, userId, bookId);
            if (purchase) {
                return new Result<>(ResultCode.USER_HAS_PURCHASE_BOOK.getIndex(), ResultCode.USER_HAS_PURCHASE_BOOK.getMsg(), null);
            }
            BigDecimal price = bookService.getBookById(bookId).getPrice();
            UserOrder userOrder = commonService.createUserOrder(userId, bookId, price, origin, null, UserOrderPayRoleStatus.NATURE_ROLE);
            userOrderService.saveUserOrder(userOrder);
            if (StringUtils.isNotEmpty(phone)) {
                commonService.updateKnowledgeGiftUserOrder(userOrder, phone);
            }
            WeChatPayUnifiedOrderParams weChatPayUnifiedOrderParams = commonService.createWeChatPayUnifiedOrderParams(userOrder, appId, mchId, "APP", null, callBackUrl, bookId, request);
            result = weChatService.signAPPPay(weChatPayUnifiedOrderParams);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    @PostMapping(value = "/weChatAppCallBack", produces = MediaType.APPLICATION_XML_VALUE)
    public void weChatAppCallBack(HttpServletRequest request, HttpServletResponse response) {
        try {
            commonService.weChatCallBackHandle(key, request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
    }

    @GetMapping(value = "/order/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> queryOrderPayStatus(@RequestParam String orderNo) {
        UserOrder userOrder = userOrderService.getUserOrderByOrderNo(orderNo);
        if (userOrder == null)
            return new Result<>(ResultCode.USER_ORDER_NOT_EXITS.getIndex(), ResultCode.USER_ORDER_NOT_EXITS.getMsg(), Boolean.FALSE);
        if (userOrder.getState().equals(UserOrderStatus.COMPLETE_PAY.getIndex()))
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        Boolean flag = weChatService.queryOrderPayStatus(orderNo);
        if (flag)
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        return new Result<>(ResultCode.USER_ORDER_PAY_FAIL.getIndex(), ResultCode.USER_ORDER_PAY_FAIL.getMsg(), Boolean.FALSE);
    }
}
