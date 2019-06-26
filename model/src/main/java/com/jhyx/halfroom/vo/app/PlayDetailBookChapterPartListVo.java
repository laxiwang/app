package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayDetailBookChapterPartListVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String name;
    private Integer totalDuration;
    private boolean video;
}
