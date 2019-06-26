package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookChapterPartCommentTypeStatus {
    TEXT(0, "文字评论"), VOICE(1, "语音评论");
    private Integer index;
    private String msg;
}
