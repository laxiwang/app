package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class UserOrder {
    private Long id;
    private String orderno;
    private Long userid;
    private Integer bookid;
    private BigDecimal fee;
    private Integer state;
    private Integer paysource;
    private Integer type;
    private String createtime;
    private String updatetime;
    private Integer payrole;
    private Long introducerid;
    private Integer branchsalerId;
}
