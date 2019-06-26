package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPlayRecordCountVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private Long userId;
    private Integer playTimeEnd;
    private Integer playCount;
}
