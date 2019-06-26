package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AppVersionStatus {
    NO_UPGRADE(0, "不需要升级"), CAN_UPGRADE(1, "升级可选"), FORCE_UPGRADE(2, "强制升级");
    private Integer index;
    private String msg;
}
