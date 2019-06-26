package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ManualTypeStatus {
    GENERAL_MANUAL(0, "知识素材"), TEACHER_MANUAL(1, "教师素材");
    private Integer index;
    private String msg;
}
