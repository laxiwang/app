package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppVersion {
    private Integer id;
    private String os;
    private Integer upGrade;
    private String version;
    private String content;
    private String packUrl;
    private String md5;
}
