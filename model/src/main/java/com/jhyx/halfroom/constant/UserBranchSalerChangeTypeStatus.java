package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserBranchSalerChangeTypeStatus {
    ADMIN_CHANGE(1, "管理员变更"), PAY_CHANGE(0, "支付变更");
    private Integer index;
    private String msg;
}
