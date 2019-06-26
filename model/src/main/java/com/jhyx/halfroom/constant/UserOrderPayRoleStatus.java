package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserOrderPayRoleStatus {
    SPREAD_ROLE(0, "推广大使推广"), SALE_ROLE(1, "销售大使推广"),
    NATURE_ROLE(2, "自然流量"), KNOWLEDGE_GIFT_ROLE(3, "知识送礼");
    private Integer index;
    private String msg;
}
