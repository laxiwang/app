package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookChapterPartStatus {
    FREE(0, "可以免费试听"), CHARGE(1, "需要购买");
    private Integer index;
    private String msg;
}
