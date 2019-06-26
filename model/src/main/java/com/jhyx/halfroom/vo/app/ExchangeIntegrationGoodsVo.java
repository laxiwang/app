package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExchangeIntegrationGoodsVo {
    private String faceThumbUrl;
    private String name;
    private Integer integration;
    private String createTime;
}
