package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityExercises {
    private Integer id;
    private String title;
    private String options;
    private String answer;
    private String tips;
    private Integer userType;
    private Integer exercisesType;
    private Integer exercisesDifficultyLevel;
}
