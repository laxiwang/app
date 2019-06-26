package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartDetailVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String name;
    private Integer duration;
    private Long playSum;
    private boolean video;
    private boolean free;
    private boolean permission;
    private String answerInfo;
}
