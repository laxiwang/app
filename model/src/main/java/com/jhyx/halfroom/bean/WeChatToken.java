package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatToken {
    private Long id;
    private String content;
    private Integer type;
    private String createTime;
    private String updateTime;
}
