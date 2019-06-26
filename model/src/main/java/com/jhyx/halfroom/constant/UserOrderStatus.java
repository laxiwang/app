package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserOrderStatus {
    PREPARE_PAY(0, "待支付"), COMPLETE_PAY(1, "已支付");
    private Integer index;
    private String msg;
}
