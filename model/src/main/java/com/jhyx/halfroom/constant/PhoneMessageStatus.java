package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhoneMessageStatus {
    VERIFICATION_CODE(2533092, "短信验证码"), PROMOTION_AMBASSADOR_INVITE_REGISTER(2873546, "推广大使邀请注册"),
    PROMOTION_AMBASSADOR_INVITE_PAY(2873558, "推广大使邀请付费"), SALES_AMBASSADOR_INVITE_REGISTER(2873564, "销售大使邀请注册"),
    SALES_AMBASSADOR_INVITE_PAY(2873570, "销售大使邀请付费");
    private Integer index;
    private String msg;
}
