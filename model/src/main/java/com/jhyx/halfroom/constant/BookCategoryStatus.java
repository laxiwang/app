package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookCategoryStatus {
    SUBJECT_ENLIGHTEN(0, "学科启蒙"), GREAT_CHINESE(1, "大语文"), UPDATE_CLASSROOM(2, "升级课堂");
    private Integer index;
    private String msg;
}
