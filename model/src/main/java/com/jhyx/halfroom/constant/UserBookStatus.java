package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserBookStatus {
    EXP(1, "体验"), VIP(2, "付费会员");
    private Integer index;
    private String msg;
}
