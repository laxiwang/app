package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayEntryStatus {
    WAP(0, "wap"), IOS(1, "ios"), ANDROID(2, "android"), CARD(3, "card");
    private Integer index;
    private String msg;
}
