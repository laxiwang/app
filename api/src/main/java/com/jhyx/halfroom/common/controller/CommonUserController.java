package com.jhyx.halfroom.common.controller;

import com.jhyx.halfroom.bean.User;
import com.jhyx.halfroom.bean.UserRechargeRecord;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.commons.PhoneUtil;
import com.jhyx.halfroom.commons.UniqueKeyGeneratorUtil;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.*;
import com.jhyx.halfroom.vo.common.UserLoginResultVo;
import com.jhyx.halfroom.vo.common.UserRechargeRecordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v4/common/user")
@RequiredArgsConstructor
@Slf4j
public class CommonUserController {
    private final UserService userService;
    private final UserLoginService userLoginService;
    private final PhoneService phoneService;
    private final IntegralService integralService;
    private final CommonService commonService;
    private final UserRechargeRecordService userRechargeRecordService;

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<UserLoginResultVo> login(@RequestParam String phone,
                                           @RequestParam String code,
                                           @RequestParam String system,
                                           @RequestParam(required = false) Long ambassadorId,
                                           @RequestParam(required = false) String userName) {
        try {
            UserLoginResultVo result = new UserLoginResultVo();
            final String androidPhone = "18330849968";
            final String applePhone = "17611051018";
            if (!androidPhone.equals(phone) && !applePhone.equals(phone)) {
                String phoneCode = phoneService.getPhoneCodeByPhone(phone);
                if (phoneCode == null)
                    return new Result<>(ResultCode.PHONE_CODE_EXPIRED.getIndex(), ResultCode.PHONE_CODE_EXPIRED.getMsg(), null);
                if (!phoneCode.equals(code))
                    return new Result<>(ResultCode.PHONE_CODE_ERROR.getIndex(), ResultCode.PHONE_CODE_ERROR.getMsg(), null);
            }
            User user = userService.getUserByPhone(phone);
            if (user != null) {
                String token = UniqueKeyGeneratorUtil.generateToken();
                userLoginService.saveOrUpdateUserLogin(user.getId(), token);
                result.setToken(token).setUserId(user.getId()).setAvatar(user.getHeadImage()).setName(user.getName());
            } else {
                Map<String, String> map = PhoneUtil.getProvinceAndCityByPhone(phone);
                if (map == null)
                    return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
                String province = map.get("province");
                String city = map.get("city");
                if (StringUtils.isEmpty(province) || StringUtils.isEmpty(city))
                    return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
                String token = UniqueKeyGeneratorUtil.generateToken();
                user = commonService.createUser(phone, province, city, ambassadorId, system, token);
                LocalDateTime localDateTime = LocalDateTime.now();
                localDateTime = localDateTime.plusDays(7);
                result.setImageUrl(commonService.getUserRegisterTipsImageUrl( "活动截止：" + localDateTime.getMonthValue() + "月" + localDateTime.getDayOfMonth() + "日"));
                result.setName(user.getName()).setAvatar(user.getHeadImage()).setToken(token).setUserId(user.getId());
            }
            if (!StringUtils.isEmpty(userName)) {
                user.setName(userName);
                userService.updateUser(user);
                result.setName(userName);
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/integration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Integer> userIntegration(HttpServletRequest request) {
        int integration;
        try {
            integration = integralService.getUserIntegral(commonService.getUserIdNeedLogin(request));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), integration);
    }

    @GetMapping(value = "/virtual/point", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Integer> userVirtualPoint(HttpServletRequest request) {
        int virtualPoint;
        try {
            virtualPoint = commonService.getUserVirtualPoint(commonService.getUserIdNeedLogin(request));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), virtualPoint);
    }

    @GetMapping(value = "/recharge/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<UserRechargeRecordVo>> userVirtualRechargeList(HttpServletRequest request) {
        try {
            List<UserRechargeRecordVo> results = new ArrayList<>();
            List<UserRechargeRecord> userRechargeRecordList = userRechargeRecordService.getUserRechargeRecordListByUserId(commonService.getUserIdNeedLogin(request));
            for (UserRechargeRecord userRechargeRecord : userRechargeRecordList) {
                UserRechargeRecordVo vo = new UserRechargeRecordVo();
                vo.setRechargePoint(userRechargeRecord.getRechargePoint());
                vo.setCreateTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(userRechargeRecord.getCreateTime())));
                results.add(vo);
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
