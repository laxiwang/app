package com.jhyx.halfroom.vo.app;

import com.jhyx.halfroom.vo.common.CommonBookChapterVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BookCourseVo {
    private Integer sectionCount;
    private boolean purchase;
    private Integer updateCount;
    private List<FreeBookChapterPartVo> freeModule;
    private List<BookChapterVo> chapterModule;
    private List<CommonBookChapterVo> bookChapterInfo;
}
