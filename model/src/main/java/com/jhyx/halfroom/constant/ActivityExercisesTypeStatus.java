package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityExercisesTypeStatus {
    HISTORY(0, "历史"), CHINESE(1, "语文");
    private Integer index;
    private String msg;
}
