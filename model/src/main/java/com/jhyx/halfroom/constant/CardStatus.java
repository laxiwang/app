package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardStatus {
    NO_ACTIVATE(0, "未激活"), HAS_ACTIVATE(1, "已激活"), HAS_USED(2, "已使用");
    private Integer index;
    private String msg;
}
