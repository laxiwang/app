package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BookChapterVo {
    private String name;
    private String title;
    private List<BookChapterPartCourseVo> list;
}
