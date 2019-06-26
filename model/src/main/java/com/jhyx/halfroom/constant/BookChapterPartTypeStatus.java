package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookChapterPartTypeStatus {
    AUDIO(0, "音频"), VIDEO(1, "视频");
    private Integer index;
    private String msg;
}
