package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Banner {
    private Integer id;
    private String imgUrl;
    private String redirectUrl;
    private Integer type;
    private String createTime;
    private String updateTime;
}
