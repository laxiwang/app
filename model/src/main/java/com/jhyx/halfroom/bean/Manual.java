package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Manual {
    private Integer id;
    private String name;
    private String faceThumbUrl;
    private Integer page;
    private String speaker;
    private Integer type;
    private String url;
    private String createTime;
    private String updateTime;
}
