package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartCourseVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String name;
    private Long playSum;
    private Integer playTimeEnd;
    private Integer totalDuration;
    private boolean free;
    private boolean permission;
    private boolean video;
    //答题的情况
    private String answerInfo;
}
