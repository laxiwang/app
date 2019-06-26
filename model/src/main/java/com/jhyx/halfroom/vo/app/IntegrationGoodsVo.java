package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IntegrationGoodsVo {
    private Integer bookId;
    private String faceThumbUrl;
    private String name;
    private String grade;
    private Integer integration;
    private Integer exchangeCount;
    private boolean flag;
    private Integer ageMax;
    private Integer ageMin;
}
