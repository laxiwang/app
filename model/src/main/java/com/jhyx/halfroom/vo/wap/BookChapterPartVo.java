package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String name;
    private Long playSum;
    private Integer duration;
    private boolean free;
    private boolean video;
    private boolean permission;
}
