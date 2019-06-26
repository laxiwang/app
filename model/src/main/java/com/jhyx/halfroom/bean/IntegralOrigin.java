package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IntegralOrigin {
    private Integer id;
    private Long userId;
    private Long inUserId;
    private Integer integral;
    private Integer type;
    private String createTime;
}
