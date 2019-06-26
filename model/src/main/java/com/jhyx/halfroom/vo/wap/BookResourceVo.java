package com.jhyx.halfroom.vo.wap;

import com.jhyx.halfroom.vo.common.CommonBookChapterVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BookResourceVo {
    private List<BookChapterPartVo> freeModule;
    private List<BookChapterVo> chapterModule;
    private List<CommonBookChapterVo> bookChapterInfo;
    private Integer status;
    private Integer courseCount;
    private Integer updateCourseCount;
}
