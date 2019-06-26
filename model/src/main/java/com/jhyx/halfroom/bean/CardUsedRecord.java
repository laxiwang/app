package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CardUsedRecord {
    private Long cardNo;
    private Integer cardType;
    private Long userId;
    private String createTime;
}
