package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IntegrationTypeStatus {
    SHARE(0, "分享赠送积分", 20), PURCHASE(1, "购买赠送积分", 200),
    EXCHANGE_COURSE(2, "兑换课程", null), PRESENT(3, "赠送积分", null),
    ANSWER_CORRECT(4, "答题赠送积分", 5);
    private Integer index;
    private String msg;
    private Integer integration;
}
