package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Advertisement {
    private Integer id;
    private String imgUrl;
    private String redirectUrl;
    private Integer status;
    private String createTime;
    private String updateTime;
}
