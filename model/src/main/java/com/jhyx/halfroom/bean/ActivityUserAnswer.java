package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityUserAnswer {
    private Long id;
    private Long userId;
    private Integer historyCount;
    private Integer chineseCount;
    private Integer exercisesType;
    private Integer recordHigh;
    private String createTime;
    private String updateTime;
}
