package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityExercisesUserTypeStatus {
    PUPIL(0, "小学生"), JUNIOR_SCHOOL_STUDENT(1, "初中生");
    private Integer index;
    private String msg;
}
