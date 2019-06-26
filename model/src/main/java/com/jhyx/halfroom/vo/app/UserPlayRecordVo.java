package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPlayRecordVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String faceThumbUrl;
    private String name;
    private Integer timeEnd;
    private boolean video;
}
