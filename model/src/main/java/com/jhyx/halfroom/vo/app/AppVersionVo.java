package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppVersionVo {
    private Integer upGrade;
    private String version;
    private String content;
    private String packUrl;
    private String md5;
}
