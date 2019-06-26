package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BookChapterVo {
    private Integer bookChapterId;
    private String chapterName;
    private List<BookChapterPartVo> parts;
}
