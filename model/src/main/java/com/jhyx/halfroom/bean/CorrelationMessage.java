package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CorrelationMessage {
    private String correlationId;
    private String orderNo;
    private Integer type;
    private String createTime;
    private String updateTime;
}
