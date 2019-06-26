package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Card {
    private Integer cardNo;
    private String cardCode;
    private BigDecimal fee;
    private Integer type;
    private Integer branchsalerId;
    private String createtime;
    private String updatetime;
    private Integer status;
}
