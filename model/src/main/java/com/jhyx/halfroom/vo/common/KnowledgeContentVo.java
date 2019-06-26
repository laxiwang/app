package com.jhyx.halfroom.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class KnowledgeContentVo {
    private String imageUrl;
    private List<CourseTestVo> courseTestList;
}
