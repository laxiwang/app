package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRechargeRecordStatus {
    NO_CHARGE(0, "没有付费"), CHARGED(1, "已付费");
    private Integer index;
    private String msg;
}
