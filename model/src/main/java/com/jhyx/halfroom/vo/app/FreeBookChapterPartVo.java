package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FreeBookChapterPartVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String name;
    private Long playSum;
    private Integer totalDuration;
    private Integer playTimeEnd;
    private boolean video;
    //答题的情况
    private String answerInfo;
}
