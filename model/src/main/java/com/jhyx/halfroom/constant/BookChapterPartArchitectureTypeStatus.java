package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookChapterPartArchitectureTypeStatus {
    KNOWLEDGE_EXTRACT(1, "知识提炼"), COURSE_TEST(2, "本讲测试");
    private Integer index;
    private String msg;
}
