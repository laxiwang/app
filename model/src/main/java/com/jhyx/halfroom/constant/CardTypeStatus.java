package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardTypeStatus {
    OFFICIAL_CARD(0, "正式卡"), EXPERIENCE_CARD(1, "半月卡");
    private Integer index;
    private String msg;
}
