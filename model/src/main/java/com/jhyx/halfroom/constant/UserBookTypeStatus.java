package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserBookTypeStatus {
    EXP(1, "三天"), VIP(2, "永久");
    private Integer index;
    private String msg;
}
