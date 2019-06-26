package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRoleStatus {
    SPREAD_AMBASSADOR(0, "推广大使"), SALE_AMBASSADOR(1, "销售大使");
    private Integer index;
    private String msg;
}
