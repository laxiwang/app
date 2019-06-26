package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegisterEntryStatus {
    WAP(0, "wap端注册"), ANDROID(1, "安卓端注册"), IOS(2, "苹果端注册"), CARD(3, "会员卡注册");
    private Integer index;
    private String msg;
}
