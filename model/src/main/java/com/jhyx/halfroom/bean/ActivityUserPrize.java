package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActivityUserPrize {
    private Integer id;
    private Long userId;
    private String userName;
    private String userAddress;
    private String userPhone;
    private String createTime;
    private String updateTime;
}
