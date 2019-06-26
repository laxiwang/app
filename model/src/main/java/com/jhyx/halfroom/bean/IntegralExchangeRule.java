package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IntegralExchangeRule {
    private Integer id;
    private Integer integral;
    private Integer bookId;
    private Integer status;
    private String createTime;
}
