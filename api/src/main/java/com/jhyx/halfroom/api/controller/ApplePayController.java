package com.jhyx.halfroom.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jhyx.halfroom.bean.User;
import com.jhyx.halfroom.bean.UserConsumeRecord;
import com.jhyx.halfroom.bean.UserOrder;
import com.jhyx.halfroom.bean.UserRechargeRecord;
import com.jhyx.halfroom.commons.UniqueKeyGeneratorUtil;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.constant.UserOrderPayRoleStatus;
import com.jhyx.halfroom.constant.UserRechargeRecordStatus;
import com.jhyx.halfroom.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URL;

@RestController
@RequestMapping(value = "/v4/api/apple")
@RequiredArgsConstructor
@Slf4j
public class ApplePayController {
    private final UserOrderService userOrderService;
    private final CommonService commonService;
    private final UserLoginService userLoginService;
    private final UserService userService;
    private final UserRechargeRecordService userRechargeRecordService;
    private final UserConsumeRecordService userConsumeRecordService;
    private final BookService bookService;
    @Value("${user.defaultAvatar}")
    private String defaultAvatar;

    @GetMapping(value = "/anonymity/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> anonymityLogin(HttpServletRequest request) {
        Long userId = commonService.getUserIdNoNeedLogin(request);
        String token = UniqueKeyGeneratorUtil.generateToken();
        if (userId == null) {
            User user = commonService.createAnonymityUser();
            userService.saveUser(user);
            userId = user.getId();
        }
        if (userId != null) {
            userLoginService.saveOrUpdateUserLogin(userId, token);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), token);
    }

    @PostMapping(value = "/pay/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> createOrder(@RequestParam Integer bookId,
                                       @RequestParam String origin,
                                       @RequestParam(required = false, defaultValue = "") String phone,
                                       HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            boolean purchase = commonService.judgeUserPurchaseBookByUserIdOrPhone(phone, userId, bookId);
            if (purchase) {
                return new Result<>(ResultCode.USER_HAS_PURCHASE_BOOK.getIndex(), ResultCode.USER_HAS_PURCHASE_BOOK.getMsg(), null);
            }
            Integer userVirtualPoint = commonService.getUserVirtualPoint(userId);
            BigDecimal price = bookService.getBookById(bookId).getPrice();
            if (userVirtualPoint < price.intValue()) {
                return new Result<>(ResultCode.VIRTUAL_POINT_DEFICIENCY.getIndex(), ResultCode.VIRTUAL_POINT_DEFICIENCY.getMsg(), null);
            }
            UserOrder userOrder = commonService.createUserOrder(userId, bookId, price, origin, null, UserOrderPayRoleStatus.NATURE_ROLE);
            userOrderService.saveUserOrder(userOrder);
            if (StringUtils.isNotEmpty(phone)) {
                commonService.updateKnowledgeGiftUserOrder(userOrder, phone);
            }
            UserConsumeRecord userConsumeRecord = new UserConsumeRecord();
            userConsumeRecord.setUserId(userId).setConsumePoint(price.intValue()).setBookId(bookId);
            userConsumeRecordService.saveUserConsumeRecord(userConsumeRecord);
            commonService.paySuccessAfter(userOrder.getOrderno());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
    }

    @GetMapping(value = "/pay/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> verifyReceipt(@RequestParam String orderNo,
                                         @RequestParam String receipt,
                                         @RequestParam(required = false, defaultValue = "https://buy.itunes.apple.com/verifyReceipt") String url) {
        final String PAY_SUCCESS_MESSAGE = "支付成功";
        final String PAY_FAIL_MESSAGE = "支付失败";
        try {
            UserRechargeRecord rechargeRecord = userRechargeRecordService.getUserRechargeRecordById(orderNo);
            if (rechargeRecord != null && rechargeRecord.getStatus().equals(UserRechargeRecordStatus.CHARGED.getIndex()))
                return new Result<>(ResultCode.SUCCESS.getIndex(), PAY_SUCCESS_MESSAGE, Boolean.TRUE);
            if (rechargeRecord != null) {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setAllowUserInteraction(false);
                PrintStream ps = new PrintStream(connection.getOutputStream());
                ps.print("{\"receipt-data\": \"" + receipt + "\"}");
                ps.close();
                String str;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((str = br.readLine()) != null)
                    sb.append(str);
                br.close();
                String resultStr = sb.toString();
                Integer status = JSONObject.parseObject(resultStr).getInteger("status");
                if (status == 0) {
                    rechargeRecord.setStatus(UserRechargeRecordStatus.CHARGED.getIndex());
                    userRechargeRecordService.updateUserRechargeRecord(rechargeRecord);
                    return new Result<>(ResultCode.SUCCESS.getIndex(), PAY_SUCCESS_MESSAGE, Boolean.TRUE);
                } else if (status == 21007) {
                    return verifyReceipt(orderNo, receipt, "https://sandbox.itunes.apple.com/verifyReceipt");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
        }
        return new Result<>(ResultCode.ERROR.getIndex(), PAY_FAIL_MESSAGE, Boolean.FALSE);
    }

    @GetMapping(value = "/restore/pay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> restorePay(@RequestParam String key) {
        try {
            User user = commonService.createAnonymityUser();
            userService.saveUser(user);
            user.setMappingNewAdd(0);
            userService.updateUser(user);
            String token = UniqueKeyGeneratorUtil.generateToken();
            userLoginService.saveOrUpdateUserLogin(user.getId(), token);
            String[] values = key.split(",");
            for (String value : values) {
                Integer bookId = commonService.getBookIdByAppleKey(value);
                if (bookId != null)
                    commonService.openCourseVipPermission(user.getId(), bookId);
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), token);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
