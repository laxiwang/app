package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRechargeRecord {
    private String id;
    private Long userId;
    private Integer rechargePoint;
    private Integer status;
    private String createTime;
    private String updateTime;
}
