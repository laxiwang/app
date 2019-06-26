package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShareInfoTypeStatus {
    WAP_HOME_PAGE(1, "wap首页分享"), WAP_KNOWLEDGE_PRESENT(2, "wap知识送礼"),
    APP_SPREAD_AMBASSADOR(3, "app推广大使分享"), APP_SALE_AMBASSADOR(4, "app销售大使分享"),
    STUDY_PUNCH_TIME_CARD(5, "学习打卡");
    private Integer index;
    private String msg;
}
