package com.jhyx.halfroom.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommonBookChapterVo {
    private String name;
    private String title;
    private Integer bookChapterId;
}
