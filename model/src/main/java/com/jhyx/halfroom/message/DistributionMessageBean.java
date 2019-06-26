package com.jhyx.halfroom.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DistributionMessageBean {
    private int userId; //用户ID
    private int branchsalerId; //分会ID
    private BigDecimal amount; //变动金额
    private String type; //变动类型: 0 收入，1，支出（提现）
    private String salerType; //推广类型: 0 推广大使，1，销售大使
    private Date createtime;
    private String orderno; //订单编号
}
