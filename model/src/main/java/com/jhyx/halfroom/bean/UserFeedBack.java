package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserFeedBack {
    private Integer id;
    private Long userId;
    private String content;
    private String createTime;
    private String updateTime;
}
