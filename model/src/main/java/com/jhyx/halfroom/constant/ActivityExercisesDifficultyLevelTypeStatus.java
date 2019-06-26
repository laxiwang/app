package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityExercisesDifficultyLevelTypeStatus {
    SIMPLE(0, "简单"), MIDDLE(1, "中等"), DIFFICULTY(2, "困难");
    private Integer index;
    private String msg;
}
