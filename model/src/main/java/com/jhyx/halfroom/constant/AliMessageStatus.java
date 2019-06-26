package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AliMessageStatus {
    VERIFICATION_CODE("SMS_166376443", "短信验证码"), PROMOTION_AMBASSADOR_INVITE_REGISTER("SMS_166376468", "推广大使邀请注册"),
    PROMOTION_AMBASSADOR_INVITE_PAY("SMS_166371447", "推广大使邀请付费"), SALES_AMBASSADOR_INVITE_REGISTER("SMS_166376457", "销售大使邀请注册"),
    SALES_AMBASSADOR_INVITE_PAY("SMS_166376461", "销售大使邀请付费");
    private String index;
    private String msg;
}
