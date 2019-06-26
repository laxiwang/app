package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPurchaseBookStatus {
    PAY(0, "购买的课程"), PRESENTATION(1, "活动赠送的资料");
    private Integer index;
    private String msg;
}
