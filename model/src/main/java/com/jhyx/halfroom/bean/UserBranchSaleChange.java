package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserBranchSaleChange {
    private Integer id;
    private Long userId;
    private Integer oldBranchsalerId;
    private Integer newBranchsalerId;
    private Integer adminId;
    private Integer type;
    private String createtime;
}
