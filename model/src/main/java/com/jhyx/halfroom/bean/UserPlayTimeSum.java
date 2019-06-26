package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPlayTimeSum {
    private Long id;
    private Long userId;
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private Long playSum;
}
