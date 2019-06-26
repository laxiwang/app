package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRechargeGoodsKey {
    private Integer rechargePoint;
    private String key;
}
