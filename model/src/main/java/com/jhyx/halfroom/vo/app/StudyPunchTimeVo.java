package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StudyPunchTimeVo {
    private Long userId;
    private String name;
    private String avatar;
    private Long studyDays;
    private Long studyMinutes;
    private Integer studyParts;
    private String percentage;
    private Long subjectEnlightenMinutes;
    private Long greatChineseMinutes;
    private Long updateClassroomMinutes;
}
