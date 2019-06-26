package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookPresentationChapterPartVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private Integer totalDuration;
    private String name;
    private boolean video;
}
