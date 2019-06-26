package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IntegralExchangeRecord {
    private Integer id;
    private Long userId;
    private Integer bookId;
    private Integer integral;
    private Integer type;
    private String createTime;
}
